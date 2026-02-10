package org.cy.micoservice.app.entity.gateway.model.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogPrintStrategyAddReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 8880213747916963501L;

  /**
   * 策略名称
   */
  @NotBlank(message = "strategyName 不能为空")
  @Size(min = 1, max = 128, message = "strategyName 长度必须在 1 ~ 128 范围")
  private String strategyName;

  /**
   * 策略类型
   * @see org.cy.micoservice.blog.gateway.facade.enums.LogPrintStrategyTypeEnum
   */
  @NotBlank(message = "strategyType 不能为空")
  @Size(min = 1, max = 32, message = "strategyType 长度必须在 1 ~ 32 范围")
  private String strategyType;

  /**
   * 策略内容
   */
  @NotBlank(message = "strategyBody 不能为空")
  @Size(min = 1, max = 512, message = "strategyBody 长度必须在 1 ~ 512 范围")
  private String strategyBody;
}