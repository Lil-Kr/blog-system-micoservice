package org.cy.micoservice.blog.entity.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_route_change_log")
public class RouteChangeLog extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -5964400619741497369L;

  private Long id;

  private Long configId;

  /**
   * 递增
   */
  private Long version;

  private String changeEvent;

  /**
   * {"before":"xxx", "after":"xxxxx"}
   */
  private String changeBody;
}