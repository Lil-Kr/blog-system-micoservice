package org.cy.micoservice.app.entity.infra.console.model.req.sys.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/4/20
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AvatarUploadReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = -4268596139987319555L;

  private MultipartFile avatarFile;

  private Long adminId;

  private String avatar;
}
