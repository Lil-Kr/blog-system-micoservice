package org.cy.micoservice.blog.framework.elasticsearch.starter.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: es配置类
 */
public class ElasticsearchConfig {

  @Value("${es.address:}")
  private String esAddress;

  @Value("${es.username:}")
  private String username;

  @Value("${es.password:}")
  private String password;

  @Bean(destroyMethod = "close")
  public RestClient restClient() {
    List<HttpHost> httpHosts = Arrays.stream(esAddress.split(","))
      .map(address -> {
        String[] parts = address.split(":");
        String hostName = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 9200;
        return new HttpHost(hostName, port, "http");
      })
      .collect(Collectors.toList());
    return RestClient.builder(httpHosts.toArray(new HttpHost[0])).build();
  }

  @Bean(destroyMethod = "close")
  public RestClientTransport restClientTransport(RestClient restClient) {
    return new RestClientTransport(restClient, new JacksonJsonpMapper());
  }

  @Bean
  public ElasticsearchClient elasticsearchClient(RestClientTransport restClientTransport) {
    return new ElasticsearchClient(restClientTransport);
  }

  @Bean
  public ElasticsearchUtil elasticsearchUtil(ElasticsearchClient elasticsearchClient) {
    return new ElasticsearchUtil(elasticsearchClient);
  }
}