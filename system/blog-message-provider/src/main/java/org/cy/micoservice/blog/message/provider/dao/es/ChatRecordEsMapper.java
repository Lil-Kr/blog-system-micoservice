package org.cy.micoservice.blog.message.provider.dao.es;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRecordEs;
import org.cy.micoservice.blog.framework.elasticsearch.starter.dto.SearchPageRequest;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: 聊天内容 mapper
 */
@Slf4j
@Repository
public class ChatRecordEsMapper {

  @Resource
  private ElasticsearchUtil elasticsearchUtil;
  @Autowired
  private MessageApplicationProperties applicationProperties;

  /**
   * 保存聊天内容
   * @param chatRecordEs
   */
  public boolean index(ChatRecordEs chatRecordEs) {
    Result result = this.indexWithResult(chatRecordEs);
    return Result.Created == result || Result.Updated == result;
  }

  /**
   * index返回结果类型
   * @param chatRecordEs
   * @return
   */
  public Result indexWithResult(ChatRecordEs chatRecordEs) {
    chatRecordEs.setId(chatRecordEs.getRelationId());
    long now = System.nanoTime();
    chatRecordEs.setCreateTime(now);
    chatRecordEs.setUpdateTime(now);
    chatRecordEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    IndexResponse response = elasticsearchUtil.indexDocument(applicationProperties.getEsChatRecordIndex(), String.valueOf(chatRecordEs.getId()),chatRecordEs);
    return response.result();
  }

  /**
   * 批量保存消息内容
   * @param chatRecordEsList
   */
  public void bulk(List<ChatRecordEs> chatRecordEsList) {
    List<Map<String, Object>> bulkList = chatRecordEsList.stream()
      .map(chatRecordEs -> {
        Map<String, Object> chatRecordEsMap = new HashMap<>();
        chatRecordEsMap.put("id", String.valueOf(chatRecordEs.getId()));
        chatRecordEsMap.put("doc", chatRecordEs);
        return chatRecordEsMap;
      })
      .collect(Collectors.toList());

    try {
      elasticsearchUtil.bulkIndexDocuments(applicationProperties.getEsChatRecordIndex(), bulkList);
    } catch (Exception e) {
      log.error("message batch save has error.");
    }
  }

  /**
   * 统计 relationId 对应的对话条数
   * @param relationId
   * @return
   */
  public Long countByRelationId(String relationId) {
    Query query = Query.of(q -> q
      .term(t -> t
        .field("relationId")
        .value(FieldValue.of(relationId))
      )
    );
    return elasticsearchUtil.countDocuments(new CountRequest.Builder().query(query).build());
  }


  /**
   * 深度分页查询逻辑
   * @param chatRecordPageReqDTO
   * @return
   */
  public SearchResponse<ChatRecordEs> searchAfter(ChatRecordPageReqDTO chatRecordPageReqDTO) throws Exception {
    List<SortOptions> sortOptions = new ArrayList<>();
    sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("seqNo").order(SortOrder.Desc))));

    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
    Query relationQuery = Query.of(q -> q.term(q1 -> q1.field("relationId")
      .value(FieldValue.of(chatRecordPageReqDTO.getRelationId()))));
    boolBuilder.filter(relationQuery);
    Query query = Query.of(q -> q.bool(boolBuilder.build()));
    // 构建查询请求
    SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
      .query(query)
      .size(chatRecordPageReqDTO.getPageSize())
      .sort(sortOptions);

    if (chatRecordPageReqDTO.getSearchOffset() != null && chatRecordPageReqDTO.getSearchOffset() > 0) {
      List<FieldValue> searchAfter = Arrays.asList(
        FieldValue.of(chatRecordPageReqDTO.getSearchOffset())
      );
      requestBuilder.searchAfter(searchAfter);
    }
    SearchPageRequest searchPageRequest = new SearchPageRequest();
    searchPageRequest.setSearchRequest(requestBuilder.build());
    searchPageRequest.setIndexName(applicationProperties.getEsChatRecordIndex());
    searchPageRequest.setPageSize(chatRecordPageReqDTO.getPageSize());
    return elasticsearchUtil.searchAfter(searchPageRequest, ChatRecordEs.class);
  }
}