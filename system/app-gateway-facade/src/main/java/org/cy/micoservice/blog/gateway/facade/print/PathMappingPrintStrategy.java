package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 基于路径的映射打印策略
 */
public class PathMappingPrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = 6611368500219250383L;

  //路径表达式
  // /api/user/**
  private String pathExpression;

  private String pathPrefix;

  public PathMappingPrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    PathMappingPrintStrategy pathMappingPrintStrategy = JSON.parseObject(super.getStrategyBody(), PathMappingPrintStrategy.class);
    this.setPathExpression(pathMappingPrintStrategy.getPathExpression());
    if (pathExpression.endsWith("**")) {
      String pathPrefix = pathExpression.substring(0, pathExpression.length() - 2);
      this.setPathPrefix(pathPrefix);
    }
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    String requestPath = logRequestDTO.getPath();
    if (pathExpression.equals(requestPath)) {
      return true;
    }
    if (pathPrefix != null) {
      return requestPath.startsWith(pathPrefix);
    }
    return false;
  }


  public String getPathExpression() {
    return pathExpression;
  }

  public void setPathExpression(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  public String getPathPrefix() {
    return pathPrefix;
  }

  public void setPathPrefix(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }
}
