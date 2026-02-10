package org.cy.micoservice.blog.entity.infra.console.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 数据字典实体
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict")
public class SysDict extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 3907370379306169949L;
  /**
   * 自增主键
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 数据字典id唯一主键
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long surrogateId;

  /**
   * 数据字典名称
   */
  private String name;

  /**
   * 备注
   */
  private String remark;

  /**
   * 删除状态, 0正常, 1删除
   */
  private Integer deleted;

  /**
   * 操作ip
   */
  private String operateIp;

}
