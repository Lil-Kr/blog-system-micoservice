package org.cy.micoservice.blog.entity.admin.model.resp.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserResp extends SysUser {

	@Serial
	private static final long serialVersionUID = 2770384490466950853L;

	private String orgName;

	private String creatorName;

	private String operatorName;

}
