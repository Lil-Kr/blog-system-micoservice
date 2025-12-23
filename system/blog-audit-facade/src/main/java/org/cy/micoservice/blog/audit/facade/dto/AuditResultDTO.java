package org.cy.micoservice.blog.audit.facade.dto;

import lombok.Data;
import org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审核结果DTO
 */
@Data
public class AuditResultDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = -5352818650213468807L;

  private Boolean success;

  private String message;

  private Object refId;

  /**
   * @see AuditResultCodeEnum
   */
  private Integer code;

  private String channelName;

  public static AuditResultDTO pass() {
    AuditResultDTO auditResultDTO = new AuditResultDTO();
    auditResultDTO.setSuccess(true);
    auditResultDTO.setCode(AuditResultCodeEnum.VALID.getCode());
    return auditResultDTO;
  }

  public static AuditResultDTO invalid(String message,Integer code) {
    AuditResultDTO auditResultDTO = new AuditResultDTO();
    auditResultDTO.setSuccess(false);
    auditResultDTO.setMessage(message);
    auditResultDTO.setCode(code);
    return auditResultDTO;
  }

  public static AuditResultDTO thirdError(String message) {
    AuditResultDTO auditResultDTO = new AuditResultDTO();
    auditResultDTO.setSuccess(false);
    auditResultDTO.setMessage(message);
    auditResultDTO.setCode(AuditResultCodeEnum.THIRD_CHECK_INVALID.getCode());
    return auditResultDTO;
  }

  /**
   * 是否是第三方对接异常
   * @return
   */
  public boolean isThirdError() {
    return AuditResultCodeEnum.THIRD_CHECK_INVALID.getCode().equals(this.getCode());
  }
}
