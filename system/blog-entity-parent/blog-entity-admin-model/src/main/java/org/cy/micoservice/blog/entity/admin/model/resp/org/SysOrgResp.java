package org.cy.micoservice.blog.entity.admin.model.resp.org;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.SysOrg;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @since 2020-11-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysOrgResp extends SysOrg {

	@Serial
	private static final long serialVersionUID = -4035181948732612880L;

	private String parentName;

	private String operatorName;
}
