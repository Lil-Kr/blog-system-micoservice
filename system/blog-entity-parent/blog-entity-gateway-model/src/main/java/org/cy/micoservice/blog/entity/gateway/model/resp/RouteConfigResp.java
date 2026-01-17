package org.cy.micoservice.blog.entity.gateway.model.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class RouteConfigResp extends RouteConfig {
  @Serial
  private static final long serialVersionUID = 1918230800508848742L;

}
