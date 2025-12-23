package org.cy.micoservice.blog.entity.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_route_change_log")
public class RouteChangeLog implements Serializable {

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

  private String createBy;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  private Integer deleted;
}