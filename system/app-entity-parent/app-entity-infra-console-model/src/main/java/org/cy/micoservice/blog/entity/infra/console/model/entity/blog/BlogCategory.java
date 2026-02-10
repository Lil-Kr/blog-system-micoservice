package org.cy.micoservice.blog.entity.infra.console.model.entity.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.cy.micoservice.blog.common.constants.CommonConstants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Lil-K
 * @since 2024-03-31
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("blog_category")
@Data
public class BlogCategory implements Serializable {

  @Serial
  private static final long serialVersionUID = -5706347817998114130L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 唯一键
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long surrogateId;

  /**
   * 编号
   */
  private String number;

  /**
   * 类别名称
   */
  private String name;

  /**
   * 颜色
   */
  @Builder.Default
  private String color = CommonConstants.DEFAULT_COLOR;

  /**
   * 状态: 未使用
   */
  private Integer status;

  /**
   * 备注
   */
  private String remark;

  /**
   * 创建人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long creatorId;

  /**
   * 操作人
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long operator;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 更改时间
   */
  private Date updateTime;

}
