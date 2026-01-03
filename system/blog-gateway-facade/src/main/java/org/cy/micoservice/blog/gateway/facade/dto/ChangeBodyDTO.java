package org.cy.micoservice.blog.gateway.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/30
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeBodyDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -8040738653677134129L;

  private RouteConfig before;

  private RouteConfig after;

}
