package org.cy.micoservice.app.entity.infra.console.model.resp.sys.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAdmin;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAdminResp extends SysAdmin {

	@Serial
	private static final long serialVersionUID = 2770384490466950853L;

	private String orgName;

	private String creatorName;

	private String operatorName;

}
