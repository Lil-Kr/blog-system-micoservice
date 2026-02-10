package org.cy.micoservice.blog.framework.elasticsearch.starter.dto;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import lombok.Data;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
@Data
public class SearchPageRequest {

  private String indexName;

  private int pageSize;

  private SearchRequest searchRequest;
}