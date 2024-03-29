package com.cy.sys.pojo.vo.org;

import com.cy.sys.pojo.entity.SysOrg;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * @author Lil-Kr
 * @since 2020-11-24
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysOrgVO extends SysOrg {

}
