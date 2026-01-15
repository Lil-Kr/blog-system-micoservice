package org.cy.micoservice.blog.message.provider.service.impl;

import co.elastic.clients.elasticsearch._types.Result;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.constants.CommonConstants;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.enums.exception.BizErrorEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.ChatBoxEs;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.ChatRelationEs;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.Content;
import org.cy.micoservice.blog.framework.elasticsearch.starter.constant.BulkIndexDocumentConstants;
import org.cy.micoservice.blog.framework.id.starter.service.IdService;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatContentDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatRelationStatusEnum;
import org.cy.micoservice.blog.message.provider.config.MessageCacheKeyBuilder;
import org.cy.micoservice.blog.message.provider.constant.MessageChatRelationConstants;
import org.cy.micoservice.blog.message.provider.dao.es.ChatRelationEsMapper;
import org.cy.micoservice.blog.message.provider.service.ChatBoxEsService;
import org.cy.micoservice.blog.message.provider.service.ChatRecordEsService;
import org.cy.micoservice.blog.message.provider.service.ChatRelationEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: char-relation 对话关系 es service
 */
@Slf4j
@Service
public class ChatRelationEsServiceImpl implements ChatRelationEsService {

  @Autowired
  private ChatRecordEsService chatRecordEsService;
  @Autowired
  private ChatRelationEsMapper chatRelationEsMapper;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private MessageCacheKeyBuilder messageCacheKeyBuilder;
  @Resource
  private IdService idService;
  @Autowired
  private ChatBoxEsService chatBoxEsService;

  /**
   * 新增 [聊天会话关系] 信息, 关注 + 打招呼
   * 新增成功时, 发送一条系统通知: [xxx 与 xxx 已成为好友, 可以开始聊天]
   * @param req
   * @return
   */
  @Override
  public boolean add(ChatRelationReqDTO req) {
    // todo: 加分布式 lock 保护
    ChatRelationEs chatRelationEs = BeanCopyUtils.convert(req, ChatRelationEs.class);
    chatRelationEs.setUserId(req.getUserId());
    chatRelationEs.setReceiverId(req.getReceiverId());
    chatRelationEs.setRelationId(String.format(MessageChatRelationConstants.RELATION_ID_FORMAT, chatRelationEs.getUserId(), chatRelationEs.getReceiverId()));
    chatRelationEs.setContent(Content.builder().type(req.getType()).body(req.getContent()).build());
    chatRelationEs.setStatus(ChatRelationStatusEnum.VALID.getCode());
    // 第一次建立会话, msgCount: 0
    chatRelationEs.setMsgCount(0L);
    long now = System.nanoTime();
    chatRelationEs.setCreateTime(now);
    chatRelationEs.setUpdateTime(now);
    chatRelationEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    chatRelationEs.setId(chatRelationEs.getRelationId());
    Result result = chatRelationEsMapper.indexWithResult(chatRelationEs);

    // 发送打招呼逻辑
    if (result.equals(Result.Created) && req.getContent() != null) {
      ImChatContentDTO contentBody = new ImChatContentDTO();
      contentBody.setBody(req.getContent());
      contentBody.setType(AuditTypeEnum.TEXT.getCode());

      ImChatReqDTO imChatReqDTO = new ImChatReqDTO();
      imChatReqDTO.setReceiverId(req.getReceiverId());
      imChatReqDTO.setContent(JSONObject.toJSONString(contentBody));
      // 第一次建立会话, seqNo: 0
      imChatReqDTO.setSeqNo(0L);
      imChatReqDTO.setSenderId(req.getUserId());
      imChatReqDTO.setRelationId(chatRelationEs.getRelationId());

      // 保存聊天记录
      chatRecordEsService.index(imChatReqDTO);
    }
    return true;
  }

  @Override
  public boolean edit(ChatRelationReqDTO req) {
    ChatRelationEs chatRelationEs = BeanCopyUtils.convert(req, ChatRelationEs.class);
    return false;
  }

  /**
   * 批量保存会话关系记录
   * @param reqDTOMap
   * @return
   */
  @Override
  public boolean bulkMap(Map<String, ImChatReqDTO> reqDTOMap) {
    long now = System.nanoTime();
    List<Map<String, Object>> eachChatRelationList = reqDTOMap.keySet().stream()
      .map(relationId -> {
        ImChatReqDTO imChatReqDTO = reqDTOMap.get(relationId);
        ImChatContentDTO imChatContentDTO = JSON.parseObject(imChatReqDTO.getContent(), ImChatContentDTO.class);

        ChatRelationEs chatRelationEs = new ChatRelationEs();
        // 保证幂等
        chatRelationEs.setId(relationId);
        chatRelationEs.setRelationId(relationId);
        chatRelationEs.setUserId(imChatReqDTO.getSenderId());
        chatRelationEs.setReceiverId(imChatReqDTO.getReceiverId());
        chatRelationEs.setStatus(ChatRelationStatusEnum.VALID.getCode());
        chatRelationEs.setContent(Content.builder().type(imChatContentDTO.getType()).body(imChatContentDTO.getBody()).build());
        chatRelationEs.setMsgCount(imChatReqDTO.getSeqNo());
        chatRelationEs.setCreateTime(now);
        chatRelationEs.setUpdateTime(now);
        chatRelationEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());

        Map<String, Object> eachItemMap = new HashMap<>();
        eachItemMap.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_ID, String.valueOf(chatRelationEs.getRelationId()));
        eachItemMap.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_DOC, chatRelationEs);
        return eachItemMap;
      })
      .collect(Collectors.toList());
    return chatRelationEsMapper.bulk(eachChatRelationList);
  }

  /**
   * chat-relation: 分页查询会话关系列表
   * @param reqDTO
   * @return
   */
  @Override
  public PageResponseDTO<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO reqDTO) {
    AssertUtil.isNotNull(reqDTO.getUserId(), BizErrorEnum.PARAM_ERROR);
    PageResponseDTO<ChatRelationRespDTO> chatRelationsPageResp = chatRelationEsMapper.listChatRelationFromPage(reqDTO);
    if (CollectionUtils.isEmpty(chatRelationsPageResp.getDataList())) return chatRelationsPageResp;

    List<ChatRelationRespDTO> chatRelationRespDTOList = chatRelationsPageResp.getDataList();

    /**
     * 根据用户id查询对话记录集合
     */
    List<ChatBoxEs> chatBoxEsList = chatBoxEsService.listByUserId(reqDTO.getUserId());
    Map<String, ChatBoxEs> chatBoxEsMap = chatBoxEsList.stream()
      // Function.identity(): 防止map中的key重复报异常
      .collect(Collectors.toMap(ChatBoxEs::getRelationId, Function.identity(), (oldVal, newVal) -> oldVal));

    /**
     * 缓存当前用户已读的offset位点
     */
    Map<String, String> msgOffsetMap = new HashMap<>();
    for (ChatRelationRespDTO chatRelationRespDTO : chatRelationRespDTOList) {
      ChatBoxEs chatBoxEs = chatBoxEsMap.get(chatRelationRespDTO.getRelationId());
      // 当前用户如果不存在收信箱, 默认未读数为0
      if (chatBoxEs == null) {
        msgOffsetMap.put(chatRelationRespDTO.getRelationId(), CommonConstants.DEFAULT_NUMBER_ZERO);
        continue;
      }
      msgOffsetMap.put(chatRelationRespDTO.getRelationId(), String.valueOf(chatBoxEs.getMsgOffset()));
      if (chatRelationRespDTO.getMsgCount() != null && chatRelationRespDTO.getMsgCount() > 0) {
        // 记录未读数
        chatRelationRespDTO.setUnReadCount(Math.max(chatRelationRespDTO.getMsgCount() - chatBoxEs.getMsgOffset(), 0));
      }
    }
    String chatBoxKey = messageCacheKeyBuilder.buildChatBoxSeqNoKey(reqDTO.getUserId());
    redisTemplate.opsForHash().putAll(chatBoxKey, msgOffsetMap);

    /**
     * 顺带将消息总数给放入缓存中, 减少后续im处理下游的压力
     */
    String cacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(reqDTO.getUserId());
    String relationCountKey = messageCacheKeyBuilder.chatRelationSeqNoKey(reqDTO.getRelationId());
    chatRelationRespDTOList.stream()
      .filter(chatRelationRespDTO -> Objects.nonNull(chatRelationRespDTO.getMsgCount()))
      .forEach(chatRelationRespDTO -> {
        // 缓存消息总数
        redisTemplate.opsForHash().put(cacheKey, chatRelationRespDTO.getRelationId(), String.valueOf(chatRelationRespDTO.getMsgCount()));

        // 将消息目前的总数缓存起来, 过期时间1h
        redisTemplate.opsForValue().set(relationCountKey, String.valueOf(chatRelationRespDTO.getMsgCount()), 1L, TimeUnit.HOURS);
      });

    redisTemplate.expire(cacheKey,24, TimeUnit.HOURS);
    log.info("chatRelationPageResp: {}", JSON.toJSONString(chatRelationsPageResp));
    return chatRelationsPageResp;
  }

  /**
   * chat-relation: 根据用户id查询会话关系详情
   * @param reqDTO
   * @return
   */
  @Override
  public ChatRelationRespDTO queryRelationInfo(ChatRelationPageReqDTO reqDTO) {
    ChatRelationEs chatRelationEs = chatRelationEsMapper.queryRelationInfo(reqDTO);
    if (chatRelationEs == null) return null;

    ChatRelationRespDTO chatRelationRespDTO = BeanCopyUtils.convert(chatRelationEs, ChatRelationRespDTO.class);
    if (chatRelationEs.getContent() != null) {
      chatRelationRespDTO.setType(chatRelationEs.getContent().getType());
      chatRelationRespDTO.setContent(chatRelationEs.getContent().getBody());
    }
    return chatRelationRespDTO;
  }

  /**
   * 根据用户id查询所有会话记录
   * @param userId
   * @return
   */
  @Override
  public List<ChatRelationRespDTO> listByUserIdOrReceiverId(Long userId) {
    List<ChatRelationEs> chatRelationEsList = chatRelationEsMapper.listByUserIdOrReceiverId(userId);
    return BeanCopyUtils.convertList(chatRelationEsList, ChatRelationRespDTO.class);
  }
}
