package org.cy.micoservice.blog.entity.infra.console.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("blog_diary")
public class BlogDiary implements Serializable {

  @Serial
  private static final long serialVersionUID = 4200541972635311080L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  /**
   * 标题
   */
  private String title;

  /**
   * 日记内容
   */
  private String content;

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