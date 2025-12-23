package org.cy.micoservice.blog.entity.base.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description:
 */
@Data
public class BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 4395953837572132402L;

  private String keyWords;

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
  private Date createTime;

  /**
   * 更改时间
   */
  private Date updateTime;

  /**
   * 是否排序 ->  0:升序, 1:降序, null:不做排序
   */
  private Integer isOrder;
}
