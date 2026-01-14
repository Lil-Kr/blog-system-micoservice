package org.cy.micoservice.blog.entity.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict_detail")
public class SysDictDetail implements Serializable {

  @Serial
  private static final long serialVersionUID = -1734025655498001710L;

  /**
   * 数据字典id主键
   */
  @TableId(value = "surrogate_id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long surrogateId;

  /**
   * 数据字典主表id
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long parentId;

  /**
   * 数据字典明细名称
   */
  private String name;

  private Integer type;

  /**
   * 备注
   */
  private String remark;

}
