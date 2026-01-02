package org.cy.micoservice.blog.message.provider.service.impl;

import jakarta.annotation.Resource;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.ChatBoxEs;
import org.cy.micoservice.blog.framework.elasticsearch.starter.constant.BulkIndexDocumentConstants;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.provider.constant.MessageChatRelationConstants;
import org.cy.micoservice.blog.message.provider.dao.es.ChatBoxEsMapper;
import org.cy.micoservice.blog.message.provider.service.ChatBoxEsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 收信箱 service
 */
@Service
public class ChatBoxEsServiceImpl implements ChatBoxEsService {

  @Resource
  private ChatBoxEsMapper chatBoxEsMapper;

  @Override
  public boolean index(ChatBoxEs chatBoxEs) {
    return chatBoxEsMapper.index(chatBoxEs);
  }

  /**
   * 批量保存收信箱消息
   * @param imChatReqDTOMap
   * @return
   */
  @Override
  public boolean bulkMap(Map<String, ImChatReqDTO> imChatReqDTOMap) {
    List<Map<String, Object>> eachRecord = imChatReqDTOMap.keySet().stream()
      .map(relationId -> {
        ImChatReqDTO imChatReqDTO = imChatReqDTOMap.get(relationId);
        return ChatBoxEs.builder()
          .id(String.format(MessageChatRelationConstants.RELATION_ID_FORMAT, relationId, imChatReqDTO.getSenderId()))
          .userId(imChatReqDTO.getSenderId())
          .relationId(relationId)
          // 更新发送人的offset
          .msgOffset(imChatReqDTO.getSeqNo())
          .updateTime(System.nanoTime())
          .deleted(DeleteStatusEnum.ACTIVE.getCode())
          .build();
      })
      .map(chatBoxEs -> {
        Map<String, Object> recordMap = new HashMap<>();
        recordMap.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_ID, chatBoxEs.getId());
        recordMap.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_DOC, chatBoxEs);
        return recordMap;
      })
      .collect(Collectors.toList());

    return chatBoxEsMapper.bulk(eachRecord);
  }

  /**
   *
   * @param chatBoxEsList
   * @return
   */
  @Override
  public boolean bulkList(List<ChatBoxEs> chatBoxEsList) {
    List<Map<String, Object>> eachRecord = chatBoxEsList.stream()
      .map(chatBoxEs -> {
        Map<String, Object> record = new HashMap<>();
        record.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_ID, chatBoxEs.getId());
        record.put(BulkIndexDocumentConstants.BULK_INDEX_NAME_DOC, chatBoxEs);
        return record;
      })
      .collect(Collectors.toList());
    return chatBoxEsMapper.bulk(eachRecord);
  }

  /**
   * 查询单个用户对应的收信箱 offset
   * @param userId
   * @param relationId
   * @return
   */
  @Override
  public ChatBoxEs get(Long userId, Long relationId) {
    return chatBoxEsMapper.get(userId, relationId);
  }

  /**
   * 支持批量查询用户的已读offset值
   * @param userId
   * @return
   */
  @Override
  public List<ChatBoxEs> listByUserId(Long userId) {
    return chatBoxEsMapper.listByUserId(userId);
  }
}
