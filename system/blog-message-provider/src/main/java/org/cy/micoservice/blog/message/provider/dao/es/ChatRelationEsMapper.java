package org.cy.micoservice.blog.message.provider.dao.es;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRelationEs;
import org.cy.micoservice.blog.framework.elasticsearch.starter.dto.SearchPageRequest;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatRelationTypeEnum;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 会话关系 dao
 */
@Slf4j
@Repository
public class ChatRelationEsMapper {

  @Resource
  private ElasticsearchUtil elasticsearchUtil;

  @Autowired
  private MessageApplicationProperties applicationProperties;

  @Autowired
  private ChatRecordEsMapper chatRecordEsMapper;

  /**
   * 会话关系建立保存index
   * @param chatRelationEs
   * @return
   */
  public boolean indexSaveChatRelation(ChatRelationEs chatRelationEs) {
    long now = System.nanoTime();
    chatRelationEs.setCreateTime(now);
    chatRelationEs.setUpdateTime(now);
    chatRelationEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    IndexResponse response = elasticsearchUtil.indexDocument(applicationProperties.getEsChatRecordIndexAlias(), String.valueOf(chatRelationEs.getId()), chatRelationEs);
    return Result.Created == response.result() || Result.Updated == response.result();
  }

  /**
   * 批量会话保存index
   * @param chatRelationEsList
   * @return
   */
  public boolean bulk(List<ChatRelationEs> chatRelationEsList) {
    long now = System.nanoTime();
    List<Map<String, Object>> eachRecordList = chatRelationEsList.parallelStream()
      .map(chatRelationEs -> {
        Map<String, Object> eachItem = new HashMap<>();
        chatRelationEs.setCreateTime(now);
        chatRelationEs.setUpdateTime(now);
        chatRelationEs.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
        eachItem.put(String.valueOf(chatRelationEs.getRelationId()), chatRelationEs);
        return eachItem;
      })
      .collect(Collectors.toList());
    try {
      BulkResponse bulkResponse = elasticsearchUtil.bulkIndexDocuments(applicationProperties.getEsChatRecordIndexAlias(), eachRecordList);
    } catch (Exception e) {
      log.error("bulk index error", e);
      return false;
    }
    return true;
  }

  /**
   * 保存最新会话内容和消息总数
   * @param chatRelationEs
   * @return
   */
  public boolean updateContentAndMsgCount(ChatRelationEs chatRelationEs) {
    Long msgCount = chatRecordEsMapper.countByRelationId(chatRelationEs.getRelationId());
    chatRelationEs.setMsgCount(msgCount);
    return this.indexSaveChatRelation(chatRelationEs);
  }

  /**
   * 分页查询 会话关系列表
   * @param request
   * @return
   */
  public PageResponse<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request) {
    SearchPageRequest pageRequest = new SearchPageRequest();
    pageRequest.setPageSize(request.getPageSize());
    pageRequest.setSearchRequest(this.buildSearchRequest(request));
    pageRequest.setIndexName(applicationProperties.getEsChatRelationIndexAlias());
    try {
      SearchResponse<ChatRelationEs> searchResponse = elasticsearchUtil.searchAfter(pageRequest, ChatRelationEs.class);
      List<Hit<ChatRelationEs>> chatRelationEsHitList = searchResponse.hits().hits();
      if (chatRelationEsHitList.isEmpty()) {
        // 没有更多数据, 退出循环
        return PageResponse.emptyPage();
      }
      List<ChatRelationRespDTO> chatRelationRespList = new ArrayList<>();
      boolean hasNext = true;
      for (Hit<ChatRelationEs> hit : chatRelationEsHitList) {
        List<FieldValue> lastOffset = hit.sort();
        long lastHitValue = lastOffset.get(0).longValue();
        if (lastOffset.isEmpty()) {
          hasNext = false;
          //到最后结尾
          break;
        }
        ChatRelationEs chatRelationEs = hit.source();
        ChatRelationRespDTO resp = BeanCopyUtils.convert(chatRelationEs, ChatRelationRespDTO.class);
        LocalDateTime updateTime = Instant.ofEpochMilli(chatRelationEs.getUpdateTime())
          .atZone(ZoneOffset.UTC)
          .toLocalDateTime();
        resp.setLatestMsgTime(updateTime);
        resp.setLastHitValue(lastHitValue);
        chatRelationRespList.add(resp);
      }

      PageResponse<ChatRelationRespDTO> pageResponse = new PageResponse<>();
      pageResponse.setPage(request.getCurrentPageNum());
      pageResponse.setSize(request.getPageSize());
      pageResponse.setDataList(chatRelationRespList);
      pageResponse.setHasNext(hasNext);
      return pageResponse;
    } catch (Exception e) {
      log.error("search error:", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * 构建es查询请求
   * @param request
   * @return
   */
  private SearchRequest buildSearchRequest(ChatRelationPageReqDTO request) {
    List<SortOptions> sortOptions = new ArrayList<>();
    sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("updateTime").order(SortOrder.Desc))));

    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

    Query userIdQuery = Query.of(q -> q.term(q1 -> q1.field("userId")
      .value(FieldValue.of(request.getUserId()))));

    Query objectIdQuery = Query.of(q -> q.term(q1 -> q1.field("receiverId")
      .value(FieldValue.of(request.getUserId()))));

    Query deleteQuery = Query.of(q -> q.term(q1 -> q1.field("deleted")
      .value(FieldValue.of(DeleteStatusEnum.ACTIVE.getCode()))));

    /**
     * 私聊查询
     * 有可能客户端没有升级 (app的版本, 因此我们需要考虑客户端的兼容性问题)
     * 企业级开发中, 应该按照app的版本号返回不同的类型会话关系数据
     */
    Query statusQuery = Query.of(q -> q.term(q1 -> q1.field("type")
      .value(FieldValue.of(ChatRelationTypeEnum.SINGLE_CHAT.getCode()))));

    List<Query> shouldQueryList = Arrays.asList(userIdQuery, objectIdQuery);
    List<Query> mustQueryList = Arrays.asList(deleteQuery, statusQuery);
    boolBuilder.should(shouldQueryList);
    boolBuilder.must(mustQueryList);
    Query query = Query.of(q -> q.bool(boolBuilder.build()));
    // 构建查询请求
    SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
      .index(applicationProperties.getEsChatRecordIndexAlias())
      .query(query)
      .size(request.getPageSize())
      .sort(sortOptions);

    if (StringUtils.isNotBlank(request.getSearchAfter())) {
      List<FieldValue> searchAfter = Arrays.asList(
        FieldValue.of(request.getSearchAfter())
      );
      requestBuilder.searchAfter(searchAfter);
    }
    return requestBuilder.build();
  }
}