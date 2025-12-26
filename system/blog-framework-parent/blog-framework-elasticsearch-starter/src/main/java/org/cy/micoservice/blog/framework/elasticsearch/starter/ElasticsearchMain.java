// package org.cy.micoservice.blog.framework.elasticsearch.starter;
//
// import co.elastic.clients.elasticsearch.ElasticsearchClient;
// import co.elastic.clients.elasticsearch._types.FieldValue;
// import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
// import co.elastic.clients.elasticsearch._types.query_dsl.Query;
// import co.elastic.clients.elasticsearch.core.GetResponse;
// import co.elastic.clients.elasticsearch.core.SearchResponse;
// import co.elastic.clients.elasticsearch.core.search.Hit;
// import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
// import co.elastic.clients.json.jackson.JacksonJsonpMapper;
// import co.elastic.clients.transport.rest_client.RestClientTransport;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import org.apache.http.HttpHost;
// import org.elasticsearch.client.RestClient;
// import java.io.IOException;
// import java.io.Serial;
// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * @Author: Lil-K
//  * @Date: 2025/12/26
//  * @Description:
//  */
// public class ElasticsearchMain {
//
//    public static void main(String[] args) throws IOException {
//        String esHost = "localhost:9200";
//        // 解析主机配置
//        List<HttpHost> httpHosts = Arrays.stream(esHost.split(","))
//                .map(address -> {
//                    String[] parts = address.split(":");
//                    String hostName = parts[0];
//                    int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 9200;
//                    return new HttpHost(hostName, port, "http");
//                })
//                .collect(Collectors.toList());
//        RestClient restClient = RestClient.builder(httpHosts.toArray(new HttpHost[0])).build();
//        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        ElasticsearchClient esClient = new ElasticsearchClient(transport);
//
//
//        String indexName = ".green-note.user.user-follower-relation";
//        GetIndexResponse getIndexResponse = esClient.indices().get(g -> g.index(indexName));
//        System.out.println(getIndexResponse.result());
//
//        GetResponse<UserFollowRelation> response1 = esClient.get(g -> g.index(indexName).id("1"), UserFollowRelation.class);
//        System.out.println(response1.source());
//
//        GetResponse<UserFollowRelation> response2 = esClient.get(g -> g.index(indexName).id("2"), UserFollowRelation.class);
//        System.out.println(response2.source());
//
//        Query query = Query.of(q -> q
//                .term(t -> t
//                        .field("userId")
//                        .value(FieldValue.of(1001))
//                )
//        );
//
//
//
//       Query.of(q -> q
//                .match(t -> t
//                        .field("userId")
//                        .query(FieldValue.of(1001))
//                )
//        );
//
//        Query.of(q -> q
//                .wildcard(t -> t
//                        .field("userId")
//                        .wildcard("")
//                        .caseInsensitive(true)
//                )
//        );
//
//        Query.of(q -> q
//                .prefix(t -> t
//                        .field("username")
//                        .value("test-name")
//                )
//        );
//
//        List<Query> mustNotQueries = new ArrayList<>();
//        List<Query> shouldQueries = new ArrayList<>();
//        List<Query> mustQueries = new ArrayList<>();
//        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
//
//        if (mustQueries != null && !mustQueries.isEmpty()) {
//            boolBuilder.must(mustQueries);
//        }
//        if (shouldQueries != null && !shouldQueries.isEmpty()) {
//            boolBuilder.should(shouldQueries);
//        }
//        if (mustNotQueries != null && !mustNotQueries.isEmpty()) {
//            boolBuilder.mustNot(mustNotQueries);
//        }
//
//        Query.of(q -> q.bool(boolBuilder.build()));
//
//        SearchResponse<UserFollowRelation> response = esClient.search(s -> s
//                        .index(indexName)
//                        .query(query)
//                        .from(0)
//                        .size(10),
//                UserFollowRelation.class
//        );
//
//        List<UserFollowRelation> userFollowRelations = response.hits().hits().stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
//        System.out.println(userFollowRelations);
//    }
// }
//
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// class UserFollowRelation implements Serializable {
//   @Serial
//   private static final long serialVersionUID = -6193526673143980760L;
//
//   private Long userId;
//   private Long followerId;
//   private Long isDeleted;
//   private Date createTime;
//   private Date updateTime;
// }