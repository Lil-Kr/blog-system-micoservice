package org.cy.micoservice.app.entity.base.model.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description:
 */
@Data
public class BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 4395953837572132402L;

  private Long userId;

  private Long adminId;

  private String keyWords;

  private Integer deleted;

  /**
   * 创建人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long createId;

  /**
   * 修改人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long updateId;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 更改时间
   */
  private LocalDateTime updateTime;

  /**
   * 是否排序 ->  0:升序, 1:降序, null:不做排序
   */
  private Integer isOrder;
}
