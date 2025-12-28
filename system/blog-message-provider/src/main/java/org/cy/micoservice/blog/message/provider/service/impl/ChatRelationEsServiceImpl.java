package org.cy.micoservice.blog.message.provider.service.impl;

import co.elastic.clients.elasticsearch._types.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRelationEs;
import org.cy.micoservice.blog.entity.message.model.provider.po.Content;
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
import org.cy.micoservice.blog.message.provider.service.ChatRecordEsService;
import org.cy.micoservice.blog.message.provider.service.ChatRelationEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

  /**
   * 新增 [聊天会话关系] 信息, 关注 + 打招呼
   * 新增成功时, 发送一条系统通知: [xxx 与 xxx 已成为好友, 可以开始聊天]
   * @param chatRelationReqDTO
   * @return
   */
  @Override
  public boolean add(ChatRelationReqDTO chatRelationReqDTO) {
    // todo: 加分布式 lock 保护
    ChatRelationEs chatRelationEs = BeanCopyUtils.convert(chatRelationReqDTO, ChatRelationEs.class);
    chatRelationEs.setUserId(chatRelationReqDTO.getReceiverId());
    chatRelationEs.setReceiverId(chatRelationReqDTO.getUserId());
    chatRelationEs.setRelationId(String.format(MessageChatRelationConstants.RELATION_ID_FORMAT, chatRelationEs.getUserId(), chatRelationEs.getReceiverId()));
    chatRelationEs.setContent(Content.builder().type(chatRelationReqDTO.getType()).body(chatRelationReqDTO.getContent()).build());
    chatRelationEs.setStatus(ChatRelationStatusEnum.VALID.getCode());
    chatRelationEs.setId(chatRelationEs.getRelationId());
    log.info("chatRelationEs: {}", chatRelationEs);
    Result result = chatRelationEsMapper.indexWithResult(chatRelationEs);

    // 发送打招呼逻辑
    if (result.equals(Result.Created) && chatRelationReqDTO.getContent() != null) {
      ImChatContentDTO contentBody = new ImChatContentDTO();
      contentBody.setBody(chatRelationReqDTO.getContent());
      contentBody.setType(AuditTypeEnum.TEXT.getCode());

      ImChatReqDTO imChatReqDTO = new ImChatReqDTO();
      imChatReqDTO.setReceiverId(chatRelationReqDTO.getReceiverId());
      imChatReqDTO.setContent(JSONObject.toJSONString(contentBody));
      // 第一次建立会话, seqNo: 0
      imChatReqDTO.setSeqNo(0L);
      imChatReqDTO.setSenderId(chatRelationReqDTO.getUserId());
      imChatReqDTO.setRelationId(chatRelationEs.getRelationId());

      chatRecordEsService.index(imChatReqDTO);
    }
    return true;
  }

  @Override
  public boolean bulk(List<ChatRelationReqDTO> chatRelationReqDTOList) {
    List<ChatRelationEs> chatRelationEsList = new ArrayList<>();

    for (ChatRelationReqDTO chatRelationReqDTO : chatRelationReqDTOList) {
      ChatRelationEs userChatRelation = BeanCopyUtils.convert(chatRelationReqDTO, ChatRelationEs.class);
      userChatRelation.setStatus(ChatRelationStatusEnum.VALID.getCode());

      ChatRelationEs chatRelationEs = BeanCopyUtils.convert(chatRelationReqDTO, ChatRelationEs.class);
      // 保证幂等
      chatRelationEs.setId(userChatRelation.getRelationId());
      chatRelationEs.setRelationId(userChatRelation.getRelationId());
      chatRelationEs.setContent(Content.builder().type(chatRelationReqDTO.getType()).body(chatRelationReqDTO.getContent()).build());
      chatRelationEs.setStatus(ChatRelationStatusEnum.VALID.getCode());
      chatRelationEs.setMsgCount(chatRelationReqDTO.getSeqNo());
      chatRelationEsList.add(chatRelationEs);
    }
    return chatRelationEsMapper.bulk(chatRelationEsList);
  }

  /**
   * chat-relation: 分页查询会话关系列表
   * @param request
   * @return
   */
  @Override
  public PageResponseDTO<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request) {
    PageResponseDTO<ChatRelationRespDTO> chatRelationsPageResp = chatRelationEsMapper.listChatRelationFromPage(request);
    List<ChatRelationRespDTO> chatRelationRespDTOList = chatRelationsPageResp.getDataList();
    if (CollectionUtils.isEmpty(chatRelationRespDTOList)) return chatRelationsPageResp;

    // 顺带将消息总数给放入缓存中, 减少后续im处理下游的压力
    String cacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(request.getUserId());
    chatRelationRespDTOList.stream()
      .filter(chatRelationRespDTO -> chatRelationRespDTO.getMsgCount() != null)
      .forEach(chatRelationRespDTO -> {
        Long msgCount = chatRelationRespDTO.getMsgCount();
        // 传入消息总数
        redisTemplate.opsForHash().put(cacheKey, chatRelationRespDTO.getRelationId(), msgCount);
      });

    redisTemplate.expire(cacheKey,24, TimeUnit.HOURS);
    log.info("chatRelationPageResp: {}", JSON.toJSONString(chatRelationsPageResp));
    return chatRelationsPageResp;
  }

  @Override
  public ChatRelationRespDTO queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    ChatRelationEs chatRelationEs = chatRelationEsMapper.queryRelationInfo(chatRelationPageReqDTO);
    if (chatRelationEs == null) return null;

    ChatRelationRespDTO chatRelationRespDTO = BeanCopyUtils.convert(chatRelationEs, ChatRelationRespDTO.class);
    if (chatRelationEs.getContent() != null) {
      chatRelationRespDTO.setType(chatRelationEs.getContent().getType());
      chatRelationRespDTO.setContent(chatRelationEs.getContent().getBody());
    }
    return chatRelationRespDTO;
  }
}
