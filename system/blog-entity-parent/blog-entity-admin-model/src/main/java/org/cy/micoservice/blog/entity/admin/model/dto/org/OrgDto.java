package org.cy.micoservice.blog.entity.admin.model.dto.org;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.entity.admin.model.entity.SysOrg;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgReq;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@ToString
public class OrgDto extends SysOrg {

  @Serial
  private static final long serialVersionUID = -4389896129961569972L;

  /**
   * 参数转换为实体类
   * @param param
   * @return
   */
  public static SysOrg paramToSysOrg(OrgReq param) {
    SysOrg org = SysOrg.builder().build();
    BeanUtils.copyProperties(param,org);
    return org;
  }

  public static SysOrg paramToSysOrg(SysOrg param) {
    SysOrg org = SysOrg.builder().build();
    BeanUtils.copyProperties(param,org);
    return org;
  }
}
