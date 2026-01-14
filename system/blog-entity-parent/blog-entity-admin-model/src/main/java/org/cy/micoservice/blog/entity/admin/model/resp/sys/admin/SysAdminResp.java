package org.cy.micoservice.blog.entity.admin.model.resp.sys.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAdminResp extends SysAdmin {

	@Serial
	private static final long serialVersionUID = 2770384490466950853L;

	private String orgName;

	private String creatorName;

	private String operatorName;

}
