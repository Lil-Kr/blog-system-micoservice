package org.cy.micoservice.blog.entity.gateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeBodyDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -1344996420476558360L;

  private RouteConfig before;

  private RouteConfig after;
}