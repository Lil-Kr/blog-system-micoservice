package org.cy.micoservice.app.framework.elasticsearch.starter.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Conflicts;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.app.framework.elasticsearch.starter.constant.BulkIndexDocumentConstants;
import org.cy.micoservice.app.framework.elasticsearch.starter.dto.SearchPageRequest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: es8+操作访问工具封装
 */
public class ElasticsearchUtil {

  private ElasticsearchClient esClient;

  public ElasticsearchUtil(ElasticsearchClient esClient) {
    this.esClient = esClient;
  }

  /**
   * 分页查询--基于searchAfter实现
   *
   * @param request   请求查询
   * @throws IOException ES 客户端异常
   */
  public <T> SearchResponse<T> searchAfter(SearchPageRequest request, Class<T> clazz){
    try {
      return esClient.search(
        request.getSearchRequest(),
        clazz
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 索引是否存在判断
   * @param indexName
   * @return
   */
  public Boolean existIndex(String indexName) {
    GetIndexResponse getIndexResponse = null;
    try {
      getIndexResponse = esClient.indices().get(g -> g.index(indexName));
      return !getIndexResponse.result().isEmpty();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 根据文档唯一id精确查询
   * @param indexName
   * @param documentId
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> T getDocumentById(String indexName, String documentId, Class<T> clazz) {
    try {
      GetResponse<T> response = esClient.get(g -> g.index(indexName).id(documentId), clazz);
      return response.source();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 精确查询
   * @param indexName
   * @param fieldName
   * @param fieldValue
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> termQuery(String indexName, String fieldName, String fieldValue, Class<T> clazz) {
    Query query = Query.of(q -> q
      .term(t -> t
        .field(fieldName)
        .value(FieldValue.of(fieldValue))
      )
    );
    return this.searchDocuments(indexName, query, clazz, 0, Integer.MAX_VALUE);
  }

  /**
   * 批量根据ID查询文档
   * @param index 索引名称
   * @param ids   要查询的ID列表
   * @return 匹配的文档列表
   */
  public <T> List<T> batchGetByIds(
    String index,
    List<String> ids,
    Class<T> clazz) throws IOException {

    // 构建ids查询
    SearchRequest request = SearchRequest.of(s -> s
      .index(index)
      .query(q -> q
        .ids(i -> i
          .values(ids)  // 设置要查询的ID列表
        )
      )
      // 可以设置查询的大小, 确保能返回所有匹配的文档
      .size(ids.size())
    );

    // 执行查询
    SearchResponse<T> response = esClient.search(request, clazz);

    // 提取查询结果
    return response.hits().hits().stream()
      .map(Hit::source)
      .collect(Collectors.toList());
  }

  /**
   * 自定义条件批量查询
   * @param index 索引名称
   * @return 匹配的文档列表
   */
  public <T> List<T> batchQuery(
    String index,
    Query query,
    Class<T> clazz) throws IOException {

    // 构建ids查询
    SearchRequest request = SearchRequest.of(s -> s
      .index(index)
      .query(query)
    );

    // 执行查询
    SearchResponse<T> response = esClient.search(request, clazz);

    // 提取查询结果
    return response.hits().hits().stream()
      .map(Hit::source)
      .collect(Collectors.toList());
  }

  /**
   * match查询
   * @param indexName
   * @param fieldName
   * @param fieldValue
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> matchQuery(String indexName, String fieldName, String fieldValue, Class<T> clazz) {
    Query query = Query.of(q -> q
      .match(t -> t
        .field(fieldName)
        .query(FieldValue.of(fieldValue))
      )
    );
    return this.searchDocuments(indexName, query, clazz, 0, Integer.MAX_VALUE);
  }

  /**
   * wildcard查询
   * @param indexName
   * @param fieldName
   * @param wildcard
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> wildcardQuery(String indexName, String fieldName, String wildcard, Class<T> clazz) {
    Query query = Query.of(q -> q
      .wildcard(t -> t
        .field(fieldName)
        .wildcard(wildcard)
        .caseInsensitive(true)
      )
    );
    return this.searchDocuments(indexName, query, clazz, 0, Integer.MAX_VALUE);
  }

  /**
   * prefix查询
   * @param indexName
   * @param fieldName
   * @param prefixValue
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> prefixQuery(String indexName, String fieldName, String prefixValue, Class<T> clazz) {
    Query query = Query.of(q -> q
      .prefix(t -> t
        .field(fieldName)
        .value(prefixValue)
        .caseInsensitive(true)
      )
    );
    return this.searchDocuments(indexName, query, clazz, 0, Integer.MAX_VALUE);
  }

  /**
   * bool查询
   * @param indexName
   * @param mustQueries
   * @param mustNotQueries
   * @param shouldQueries
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> boolQuery(String indexName,
                               List<Query> mustQueries,
                               List<Query> mustNotQueries,
                               List<Query> shouldQueries,
                               Class<T> clazz) {
    return this.boolQuery(indexName, mustQueries, mustNotQueries, shouldQueries,clazz,1000);
  }

  /**
   * bool查询
   * @param indexName
   * @param mustQueries
   * @param mustNotQueries
   * @param shouldQueries
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> boolQuery(String indexName,
                               List<Query> mustQueries,
                               List<Query> mustNotQueries,
                               List<Query> shouldQueries,
                               Class<T> clazz,
                               Integer maxSize) {
    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

    if (mustQueries != null && !mustQueries.isEmpty()) {
      boolBuilder.must(mustQueries);
    }
    if (shouldQueries != null && !shouldQueries.isEmpty()) {
      boolBuilder.should(shouldQueries);
    }
    if (mustNotQueries != null && !mustNotQueries.isEmpty()) {
      boolBuilder.mustNot(mustNotQueries);
    }

    Query query = Query.of(q -> q.bool(boolBuilder.build()));
    return this.searchDocuments(indexName, query, clazz, 0, maxSize);
  }

  /**
   * 批量文档查询
   * @param indexName
   * @param query
   * @param clazz
   * @param from
   * @param size
   * @param <T>
   * @return
   */
  public <T> List<T> searchDocuments(String indexName, Query query, Class<T> clazz, int from, int size) {
    try {
      SearchResponse<T> response = esClient.search(s -> s
          .index(indexName)
          .query(query)
          .from(0)
          .size(10),
        clazz
      );
      return response.hits().hits().stream()
        .map(Hit::source)
        .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 统计总数
   * @param countRequest
   * @return
   */
  public long countDocuments(CountRequest countRequest) {
    try {
      CountResponse response = esClient.count(countRequest);
      return response.count();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 删除文档
   * @param indexName 索引名称
   * @param id        文档ID
   * @return 删除结果
   * @throws IOException 异常
   */
  public DeleteResponse deleteDocument(String indexName, String id) {
    try {
      return esClient.delete(d -> d
        .index(indexName)
        .id(id)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 更新文档
   * @param indexName 索引名称
   * @param id        文档ID
   * @param doc       要更新的字段
   * @return 更新结果
   * @throws IOException 异常
   */
  public UpdateResponse<Void> updateDocument(String indexName, String id, Map<String, Object> doc) {
    try {
      return esClient.update(u -> u
          .index(indexName)
          .id(id)
          .doc(doc),
        Void.class
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * 新增或更新文档
   * @param indexName 索引名称
   * @param id        文档ID
   * @param document  文档内容
   * @return 操作结果
   * @throws IOException 异常
   */
  public IndexResponse indexDocument(String indexName, String id, Object document) {
    try {
      return esClient.index(i -> i
        .index(indexName)
        .id(id)
        .document(document)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 自定义条件更新文档
   * @param indexName    索引名称
   * @param query        用户ID(被关注者)
   * @param updateFields 要更新的字段和值
   * @return 更新结果
   * @throws IOException 异常
   */
  public UpdateByQueryResponse updateByQuery(String indexName, Query query,
                                             Map<String, Object> updateFields, Conflicts conflicts) {
    // 构建更新脚本参数
    Map<String, JsonData> params = updateFields.entrySet().stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        entry -> JsonData.of(entry.getValue())
      ));

    // 构建更新脚本
    Script script = Script.of(s -> s
      .inline(i -> i
        .source(buildUpdateFieldScript(updateFields.keySet()))
        .params(params)
      )
    );

    // 执行更新操作
    try {
      return esClient.updateByQuery(u -> u
        .index(indexName)
        .query(query)
        .script(script)
        .refresh(false) // 根据集群的默认配置做刷新, 不要手动强制刷新, 会对es集群性能造成影响
        .conflicts(conflicts));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 批量新增或更新文档
   * @param indexName 索引名称
   * @param documents 文档列表, Map中需包含"id"字段和"doc"字段
   * @return 批量操作结果
   * @throws IOException 异常
   */
  public BulkResponse bulkIndexDocuments(String indexName, List<Map<String, Object>> documents) {
    List<BulkOperation> operations = new ArrayList<>();

    for (Map<String, Object> doc : documents) {
      String id = Objects.toString(doc.get(BulkIndexDocumentConstants.BULK_INDEX_NAME_ID), null);
      Object document = doc.get(BulkIndexDocumentConstants.BULK_INDEX_NAME_DOC);

      if (StringUtils.isBlank(id) || document == null) {
        continue;
      }

      operations.add(BulkOperation.of(o -> o
        .index(i -> i
          .index(indexName)
          .id(id)
          .document(document)
        )
      ));
    }

    try {
      return esClient.bulk(b -> b.operations(operations));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 构建Elasticsearch更新脚本
   * 根据要更新的字段列表生成脚本字符串, 支持普通字段和嵌套字段
   * @param fieldNames 要更新的字段名称集合
   * @return 生成的更新脚本字符串
   */
  private String buildUpdateFieldScript(Set<String> fieldNames) {
    // 使用StringBuilder高效构建脚本
    StringBuilder scriptBuilder = new StringBuilder();

    for (String fieldName : fieldNames) {
      // 处理嵌套字段(如"user.name"、"address.city")
      // Elasticsearch中访问嵌套字段使用点符号, 与字段名格式一致
      String sourceField = "ctx._source." + fieldName;
      // 参数引用格式：params.字段名
      String paramReference = "params." + fieldName;

      // 拼接脚本语句：ctx._source.字段名 = params.字段名;
      scriptBuilder.append(sourceField)
        .append(" = ")
        .append(paramReference)
        .append(";");
    }

    return scriptBuilder.toString();
  }
}
