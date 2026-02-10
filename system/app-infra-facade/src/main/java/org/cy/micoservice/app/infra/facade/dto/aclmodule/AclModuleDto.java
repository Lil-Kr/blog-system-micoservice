package org.cy.micoservice.app.infra.facade.dto.aclmodule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.infra.facade.dto.acl.AclDto;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAclModule;
import org.springframework.beans.BeanUtils;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  权限模块Dto
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AclModuleDto extends SysAclModule {

  @Serial
  private static final long serialVersionUID = -7578690008382294427L;

  private List<AclModuleDto> aclModuleDtoList = Lists.newArrayList();

  /**
   * 权限点数据
   */
  private List<AclDto> aclDtoList = new ArrayList<>();

  /**
   * 将权限模块数据转换为一颗树形结构
   * @return
   */
  public static AclModuleDto adapt(SysAclModule aclModule){
    AclModuleDto dto = new AclModuleDto();
    BeanUtils.copyProperties(aclModule, dto);
    return dto;
  }
}
