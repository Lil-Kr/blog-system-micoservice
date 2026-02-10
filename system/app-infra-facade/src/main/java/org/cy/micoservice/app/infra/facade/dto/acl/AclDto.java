package org.cy.micoservice.app.infra.facade.dto.acl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAcl;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AclDto extends SysAcl {

  @Serial
  private static final long serialVersionUID = 3854771651898191395L;
  /**
   * 前端是否默认要选中的样式
   */
  private boolean checked = false;

  /**
   * 是否有权限操作
   * 一个用户在分配权限时, 是不能超过当前分配已有权限的上线
   */
  private boolean hasAcl = false;

  public static AclDto adapt(SysAcl acl) {
    AclDto aclDto = AclDto.builder().build();
    BeanUtils.copyProperties(acl,aclDto);
    return aclDto;
  }
}
