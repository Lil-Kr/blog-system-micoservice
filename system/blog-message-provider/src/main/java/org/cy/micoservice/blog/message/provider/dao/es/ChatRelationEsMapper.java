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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.pojo.es.ChatRelationEs;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 会话关系 mapper
 */
@Slf4j
@Repository
public class ChatRelationEsMapper {

  @Autowired
  private MessageApplicationProperties applicationProperties;
  @Resource
  private ElasticsearchUtil elasticsearchUtil;
  @Autowired
  private ChatRecordEsMapper chatRecordEsMapper;

  /**
   * 保存会话关系建立 index
   * @param chatRelationEs
   * @return
   */
  public boolean index(ChatRelationEs chatRelationEs) {
    Result result = this.indexWithResult(chatRelationEs);
    return Result.Created == result || Result.Updated == result;
  }

  /**
   * index 返回结果类型
   * @param chatRelationEs
   * @return
   */
  public Result indexWithResult(ChatRelationEs chatRelationEs) {
    log.info("chatRelationEs: {}", JSONObject.toJSONString(chatRelationEs));
    IndexResponse response = elasticsearchUtil.indexDocument(applicationProperties.getEsChatRelationIndex(), String.valueOf(chatRelationEs.getId()), chatRelationEs);
    return response.result();
  }

  /**
   * 保存批量会话信息index
   * @param list
   * @return
   */
  public boolean bulk(List<Map<String, Object>> list) {
    try {
      BulkResponse bulkResponse = elasticsearchUtil.bulkIndexDocuments(applicationProperties.getEsChatRelationIndex(), list);
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
    return this.index(chatRelationEs);
  }

  /**
   * 分页查询 会话关系列表
   * @param request
   * @return
   */
  public PageResponseDTO<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request) {
    SearchPageRequest pageRequest = new SearchPageRequest();
    pageRequest.setSearchRequest(this.buildSearchRequest(request));
    try {
      /**
       * searchAfter:
       * 这个api性能好一些, 查询上一页末尾的最后一条记录, 在此基础上, 分发到各个ES节点中进行查询, 由各个将数据排序,
       * 最后返回这一部分的内容给到ES的master节点进行组装, 最后返回
       */
      SearchResponse<Object> searchResponse = elasticsearchUtil.searchAfter(pageRequest, Object.class);
      List<Hit<Object>> chatRelationEsHitList = searchResponse.hits().hits();
      // 没有更多数据, 退出循环
      if (chatRelationEsHitList.isEmpty()) return PageResponseDTO.emptyPage();

      List<ChatRelationRespDTO> chatRelationRespList = new ArrayList<>();
      boolean hasNext = true;
      Long latestOffset = null;
      for (Hit<Object> hit : chatRelationEsHitList) {
        List<FieldValue> lastOffset = hit.sort();
        long lastHitValue = lastOffset.get(0).longValue();
        latestOffset = lastHitValue;
        if (lastOffset.isEmpty()) {
          hasNext = false;
          // 到最后结尾
          break;
        }
        Object hitObj = hit.source();
        ChatRelationEs chatRelationEs = JSON.parseObject(JSON.toJSONString(hitObj), ChatRelationEs.class);
        ChatRelationRespDTO respDTO = BeanCopyUtils.convert(chatRelationEs, ChatRelationRespDTO.class);
        if (chatRelationEs.getContent() != null) {
          respDTO.setContent(chatRelationEs.getContent().getBody());
          respDTO.setType(chatRelationEs.getContent().getType());
        }

        ZoneId eastEightZone = ZoneId.of("Asia/Shanghai");
        LocalDateTime latestMsgTime = Instant.ofEpochMilli(chatRelationEs.getUpdateTime())
          .atZone(eastEightZone)
          .toLocalDateTime();
        respDTO.setLatestMsgTime(latestMsgTime);
        chatRelationRespList.add(respDTO);
      }

      PageResponseDTO<ChatRelationRespDTO> pageResponseDTO = new PageResponseDTO<>();
      pageResponseDTO.setPage(request.getCurrentPageNum());
      pageResponseDTO.setSize(request.getPageSize());
      pageResponseDTO.setDataList(chatRelationRespList);
      pageResponseDTO.setHasNext(hasNext);
      pageResponseDTO.setSearchOffset(latestOffset);
      return pageResponseDTO;
    } catch (Exception e) {
      log.error("chat-relation search error:", e);
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

    Query receiverIdQuery = Query.of(q -> q.term(q1 -> q1.field("receiverId")
      .value(FieldValue.of(request.getUserId()))));

    Query deleteQuery = Query.of(q -> q.term(q1 -> q1.field("deleted")
      .value(FieldValue.of(DeleteStatusEnum.ACTIVE.getCode()))));

    /**
     * 私聊查询
     * 有可能客户端没有升级 (app的版本, 因此我们需要考虑客户端的兼容性问题)
     * 企业级开发中, 应该按照app的版本号返回不同的类型会话关系数据
     */
    Query statusQuery = Query.of(q -> q.nested(q1 -> q1.path("content")
      .query(q3 -> q3.term(type ->
        type
          .field("content.type")
          .value(FieldValue.of(ChatRelationTypeEnum.SINGLE_CHAT.getCode()))
        )
      ))
    );

    List<Query> shouldQueryList = Arrays.asList(userIdQuery, receiverIdQuery);
    List<Query> mustQueryList = Arrays.asList(deleteQuery, statusQuery);
    boolBuilder.should(shouldQueryList);
    boolBuilder.must(mustQueryList);
    Query query = Query.of(q -> q.bool(boolBuilder.build()));
    // 构建查询请求
    SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
      .index(applicationProperties.getEsChatRelationIndex())
      .query(query)
      .size(request.getPageSize())
      .sort(sortOptions);

    if (StringUtils.isNotBlank(request.getSearchAfter())) {
      List<FieldValue> searchAfter = Arrays.asList(FieldValue.of(request.getSearchAfter()));
      requestBuilder.searchAfter(searchAfter);
    }
    return requestBuilder.build();
  }

  /**
   * 查询单个会话关系记录
   * @param chatRelationPageReqDTO
   * @return
   */
  public ChatRelationEs queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    if (chatRelationPageReqDTO == null || chatRelationPageReqDTO.getRelationId() == null) return null;

    List<Object> chatRelationEsList = elasticsearchUtil.termQuery(applicationProperties.getEsChatRelationIndex(), "relationId",
      chatRelationPageReqDTO.getRelationId(), Object.class);

    if (CollectionUtils.isEmpty(chatRelationEsList)) return null;
    Object hitValue = chatRelationEsList.get(0);
    return JSON.parseObject(JSON.toJSONString(hitValue), ChatRelationEs.class);
  }

  /**
   * 查询会话关系记录列表
   * @param userId
   * @return
   */
  public List<ChatRelationEs> listByUserIdOrReceiverId(Long userId) {
    Query deleteQuery = Query.of(q -> q
      .term(t -> t
        .field("deleted")
        .value(DeleteStatusEnum.ACTIVE.getCode())
      )
    );
    List<Query> mustQueryList = Arrays.asList(deleteQuery);
    Query userIdTermQuery = Query.of(q -> q
      .term(t -> t
        .field("userId")
        .value(userId)
      )
    );

    Query receiverIdTermQuery = Query.of(q -> q
      .term(t -> t
        .field("receiverId")
        .value(userId)
      )
    );

    List<Query> shouldQueryList = Arrays.asList(userIdTermQuery, receiverIdTermQuery);
    List<Object> chatRelationEsList = elasticsearchUtil.boolQuery(applicationProperties.getEsChatRelationIndex(), mustQueryList,
      null, shouldQueryList, Object.class);
    if (CollectionUtils.isEmpty(chatRelationEsList)) return null;

    List<ChatRelationEs> resultList = new ArrayList<>();
    for (Object hitObject : chatRelationEsList) {
      ChatRelationEs chatRelationEs = JSON.parseObject(JSON.toJSONString(hitObject), ChatRelationEs.class);
      resultList.add(chatRelationEs);
    }
    return resultList;
  }
}