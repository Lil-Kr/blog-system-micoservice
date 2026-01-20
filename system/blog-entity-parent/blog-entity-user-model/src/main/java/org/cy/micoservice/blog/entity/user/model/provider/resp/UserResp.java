package org.cy.micoservice.blog.entity.user.model.provider.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.user.model.provider.pojo.User;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResp extends User {

	@Serial
	private static final long serialVersionUID = 8454720158255294228L;

	private String orgName;

	private String creatorName;

	private String operatorName;

}
