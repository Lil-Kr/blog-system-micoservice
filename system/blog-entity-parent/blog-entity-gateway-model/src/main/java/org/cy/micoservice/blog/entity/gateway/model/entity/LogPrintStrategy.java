package org.cy.micoservice.blog.entity.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.common.enums.biz.ValidStatusEnum;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;
import org.cy.micoservice.blog.entity.gateway.model.enums.LogPrintStrategyTypeEnum;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 网关日志打印策略
 */
@Data
@TableName("t_log_print_strategy")
@EqualsAndHashCode(callSuper = true)
public class LogPrintStrategy extends BaseEntity {

  @Serial
  private static final long serialVersionUID = 1839080721606675973L;

  @TableId
  private Long id;

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
   * 策略内容
   */
  private String strategyBody;

  /**
   * 状态: 是否生效
   * @see ValidStatusEnum
   */
  private Integer status;
}