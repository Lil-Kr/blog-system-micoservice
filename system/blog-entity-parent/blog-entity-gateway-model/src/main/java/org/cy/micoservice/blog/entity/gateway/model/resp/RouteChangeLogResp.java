package org.cy.micoservice.blog.entity.gateway.model.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class RouteChangeLogResp extends RouteChangeLog {

  @Serial
  private static final long serialVersionUID = 4166929363565299017L;

}