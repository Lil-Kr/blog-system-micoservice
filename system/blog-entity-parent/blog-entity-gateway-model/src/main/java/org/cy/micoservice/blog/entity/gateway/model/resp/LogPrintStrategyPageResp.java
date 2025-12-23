package org.cy.micoservice.blog.entity.gateway.model.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;

import java.io.Serial;


/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogPrintStrategyPageResp extends LogPrintStrategy {

  @Serial
  private static final long serialVersionUID = 433150542738530423L;

}