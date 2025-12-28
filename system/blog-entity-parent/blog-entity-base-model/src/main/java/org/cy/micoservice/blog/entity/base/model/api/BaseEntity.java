package org.cy.micoservice.blog.entity.base.model.api;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description: BaseEntity
 */
@Data
public class BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -3071591161451842453L;

  @TableLogic(delval = "id", value = "0")
  private Integer deleted;

  /**
   * 创建人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long creatorId;

  /**
   * 修改人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long operatorId;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 更改时间
   */
  private LocalDateTime updateTime;
}
