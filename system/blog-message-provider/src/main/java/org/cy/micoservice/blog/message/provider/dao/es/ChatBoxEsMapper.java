package org.cy.micoservice.blog.message.provider.dao.es;

import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.ChatBoxEs;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Slf4j
@Repository
public class ChatBoxEsMapper {

  @Resource
  private ElasticsearchUtil elasticsearchUtil;
  @Resource
  private MessageApplicationProperties applicationProperties;

  /**
   * 保存单个用户的收信箱
   * @param chatBoxEs
   * @return
   */
  public boolean index(ChatBoxEs chatBoxEs) {
    IndexResponse indexResponse = elasticsearchUtil.indexDocument(applicationProperties.getEsChatBoxIndex(),
      String.valueOf(chatBoxEs.getId()),chatBoxEs);
    return Result.Created.equals(indexResponse.result()) || Result.Updated.equals(indexResponse.result());
  }

  /**
   * 批量更新收信箱信息
   * @param eachRecord
   * @return
   */
  public boolean bulk(List<Map<String, Object>> eachRecord) {
    try {
      elasticsearchUtil.bulkIndexDocuments(applicationProperties.getEsChatBoxIndex(), eachRecord);
      return true;
    } catch (IOException e) {
      log.error("bulk chat-box error: ", e);
    }
    return false;
  }

  /**
   * 查询单个用户对应的收信箱 offset
   * @param userId
   * @param relationId
   * @return
   */
  public ChatBoxEs get(Long userId, Long relationId) {
    List<Query> queryList = new ArrayList<>();
    Query userIdTermQuery = Query.of(q -> q
      .term(t -> t
        .field("userId")
        .value(userId)
      )
    );
    Query relationidTermQuery = Query.of(q -> q
      .term(t -> t
        .field("relationId")
        .value(relationId)
      )
    );
    Query deletedTermQuery = Query.of(q -> q
      .term(t -> t
        .field("deleted")
        .value(DeleteStatusEnum.ACTIVE.getCode())
      )
    );
    queryList.add(userIdTermQuery);
    queryList.add(deletedTermQuery);
    queryList.add(relationidTermQuery);
    List<ChatBoxEs> chatBoxEsList = elasticsearchUtil.boolQuery(applicationProperties.getEsChatBoxIndex(),
      queryList, null, null, ChatBoxEs.class);

    if (CollectionUtils.isEmpty(chatBoxEsList)) return null;

    // 有且仅有一条数据
    return chatBoxEsList.get(0);
  }

  /**
   * 读取用户的未读消息offset数据
   * @param userId
   * @return
   */
  public List<ChatBoxEs> listByUserId(Long userId) {
    Query userIdTermQuery = Query.of(q -> q
      .term(t -> t
        .field("userId").value(userId)
      )
    );
    Query deletedTermQuery = Query.of(q -> q
      .term(t -> t
        .field("deleted")
        .value(DeleteStatusEnum.ACTIVE.getCode())
      )
    );
    List<Query> mustList = Arrays.asList(userIdTermQuery, deletedTermQuery);
    List<ChatBoxEs> chatBoxEsList = elasticsearchUtil.boolQuery(applicationProperties.getEsChatBoxIndex(), mustList,
      null, null, ChatBoxEs.class);
    if (CollectionUtils.isEmpty(chatBoxEsList)) return new ArrayList<>();
    return chatBoxEsList;
  }
}