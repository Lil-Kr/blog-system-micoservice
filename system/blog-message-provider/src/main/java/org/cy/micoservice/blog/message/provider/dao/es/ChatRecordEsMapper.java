package org.cy.micoservice.blog.message.provider.dao.es;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRecordEs;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
@Slf4j
@Repository
public class ChatRecordEsMapper {

  @Resource
  private ElasticsearchUtil elasticsearchUtil;

  @Autowired
  private MessageApplicationProperties applicationProperties;

  /**
   * 保存索引内容
   * @param chatRecordEs
   */
  public boolean indexRecord(ChatRecordEs chatRecordEs) {
    long now = System.nanoTime();
    chatRecordEs.setCreateTime(now);
    chatRecordEs.setUpdateTime(now);
    chatRecordEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    IndexResponse response = elasticsearchUtil.indexDocument(applicationProperties.getEsChatRecordIndexAlias(), String.valueOf(chatRecordEs.getId()),chatRecordEs);
    return Result.Created == response.result() || Result.Updated == response.result();
  }

  /**
   * 批量保存消息内容
   * @param chatRecordEsList
   */
  public void bulk(List<ChatRecordEs> chatRecordEsList) {
    List<Map<String, Object>> bulkList = chatRecordEsList.stream()
      .map(chatRecordEs -> {
        Map<String, Object> chatRecordEsMap = new HashMap<>();
        chatRecordEsMap.put(String.valueOf(chatRecordEs.getId()), chatRecordEs);
        return chatRecordEsMap;
      })
      .collect(Collectors.toList());

    try {
      elasticsearchUtil.bulkIndexDocuments(applicationProperties.getEsChatRecordIndexAlias(), bulkList);
    } catch (Exception e) {
      log.error("message batch save has error.");
    }
  }


  /**
   * 统计 relationId 对应的对话条数
   * @param relationId
   * @return
   */
  public Long countByRelationId(Long relationId) {
    Query query = Query.of(q -> q
      .term(t -> t
        .field("relationId")
        .value(FieldValue.of(relationId))
      )
    );
    CountRequest countRequest = new CountRequest.Builder().query(query).build();
    return elasticsearchUtil.countDocuments(countRequest);
  }
}