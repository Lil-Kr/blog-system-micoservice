package org.cy.micoservice.blog.message.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRecordEs;
import org.cy.micoservice.blog.framework.id.starter.service.IdService;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatContentDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.provider.dao.es.ChatRecordEsMapper;
import org.cy.micoservice.blog.message.provider.service.ChatRecordEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: 消息记录服务 基于es存储
 */
@Service
public class ChatRecordEsServiceImpl implements ChatRecordEsService {

  @Autowired
  private IdService idService;
  @Autowired
  private ChatRecordEsMapper chatRecordEsMapper;

  @Override
  public boolean index(ImChatReqDTO imChatReqDTO) {
    ChatRecordReqDTO chatRecordReqDTO = new ChatRecordReqDTO();
    chatRecordReqDTO.setReceiverId(imChatReqDTO.getReceiverId());
    chatRecordReqDTO.setUserId(imChatReqDTO.getSenderId());
    chatRecordReqDTO.setRelationId(imChatReqDTO.getRelationId());
    // 消息对话内容
    ImChatContentDTO imChatReqContentDTO = JSON.parseObject(imChatReqDTO.getContent(), ImChatContentDTO.class);
    chatRecordReqDTO.setContent(imChatReqContentDTO.getBody());
    chatRecordReqDTO.setType(imChatReqContentDTO.getType());
    /**
     * todo: 每次保存时都查询一次db, db访问压力比较大
     * 自增的消息 offset
     */
    chatRecordReqDTO.setSeqNo(0);
    // 对话id
    chatRecordReqDTO.setChatId(idService.getId());
    chatRecordReqDTO.setStatus(imChatReqDTO.getMsgStatus());

    ChatRecordEs chatRecordEs = BeanCopyUtils.convert(chatRecordReqDTO, ChatRecordEs.class);
    chatRecordEs.setId(chatRecordReqDTO.getChatId());
    // 会话id唯一
    return chatRecordEsMapper.indexRecord(chatRecordEs);
  }

  @Override
  public void bulk(List<ImChatReqDTO> imChatReqDTOList) {
    List<ChatRecordEs> chatRecordEsList = imChatReqDTOList.stream()
      .map(imChatReq -> {
        ChatRecordReqDTO chatRecordReqDTO = new ChatRecordReqDTO();
        chatRecordReqDTO.setReceiverId(imChatReq.getReceiverId());
        chatRecordReqDTO.setUserId(imChatReq.getSenderId());
        chatRecordReqDTO.setRelationId(imChatReq.getRelationId());
        // 消息对话内容
        ImChatContentDTO imChatReqContentDTO = JSON.parseObject(imChatReq.getContent(), ImChatContentDTO.class);
        chatRecordReqDTO.setContent(imChatReqContentDTO.getBody());
        chatRecordReqDTO.setType(imChatReqContentDTO.getType());
        /**
         * todo: 不推荐每次保存时候都查询一次db, db的访问压力太大了
         * 自增的消息 offset
         * 每次保存的时候, 先查询缓存, 如果缓存miss, 则查询db --> 还是有一部分请求到DB
         * 在外层会话列表展示的时候, 提前将消息总数缓存到redis中, 用纯数字的方式保存, 因为有可能加载到缓存之后, 此时需要做消息的increase的数字
         * 假设一个人有 500 个好友, key -> number, <userId, Map<relationId, count>>, 每次刷新加护列表时, 可以做ttl操作
         */
        chatRecordReqDTO.setSeqNo(imChatReq.getSeqNo());
        // 对话id
        chatRecordReqDTO.setChatId(idService.getId());
        chatRecordReqDTO.setStatus(imChatReq.getMsgStatus());
        ChatRecordEs chatRecordEs = BeanCopyUtils.convert(chatRecordReqDTO, ChatRecordEs.class);
        chatRecordEs.setId(chatRecordReqDTO.getChatId());
        return chatRecordEs;
      })
      .collect(Collectors.toList());

    // 写入不一定立马可以查询到
    chatRecordEsMapper.bulk(chatRecordEsList);
  }

  @Override
  public PageResponse<ChatRecordRespDTO> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO) {
    return null;
  }
}
