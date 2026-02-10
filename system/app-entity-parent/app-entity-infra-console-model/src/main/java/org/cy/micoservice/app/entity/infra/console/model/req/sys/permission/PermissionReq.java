package org.cy.micoservice.app.entity.infra.console.model.req.sys.permission;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionReq extends BaseReq {
  @Serial
  private static final long serialVersionUID = 1637902099970849298L;

}
