package org.cy.micoservice.blog.user.provider.pojo.resp;

import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.user.provider.pojo.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserResp extends SysUser {

	private static final long serialVersionUID = -1483631102219847076L;

	private String orgName;

	private String creatorName;

	private String operatorName;

}
