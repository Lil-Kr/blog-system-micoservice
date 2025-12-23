package org.cy.micoservice.blog.entity.admin.model.dto.org;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.SysOrg;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgLevelDto extends SysOrg {

  @Serial
  private static final long serialVersionUID = 1425058214597424363L;

  private Long parentSurrogateId;

  private List<OrgLevelDto> orgList = Lists.newArrayList();

  /**
   * 将组织数据转换为一颗树形结构
   * @param org
   * @return
   */
  public static OrgLevelDto adapt(SysOrg org){
    OrgLevelDto dto = new OrgLevelDto();
    BeanUtils.copyProperties(org,dto);
    return dto;
  }


}
