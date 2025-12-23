package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.common.enums.biz.ValidStatusEnum;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.cy.micoservice.blog.entity.gateway.model.enums.LogPrintStrategyTypeEnum;

import java.io.Serial;


/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogPrintStrategyPageReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = 433150542738530423L;

  /**
   * 策略名称
   */
  private String strategyName;

  /**
   * 策略类型
   * @see LogPrintStrategyTypeEnum
   */
  private String strategyType;

  /**
   * 状态: 是否生效
   * @see ValidStatusEnum
   */
  private Integer status;
}