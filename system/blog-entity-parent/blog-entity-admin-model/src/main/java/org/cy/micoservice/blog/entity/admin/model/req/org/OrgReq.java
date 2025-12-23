package org.cy.micoservice.blog.entity.admin.model.req.org;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class OrgReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -1707847984968748886L;

  public interface GroupAdd {}
  public interface GroupEdit {}

  /**
   * 自增主键
   */
//    private Long id;

  /**
   * 组织主键
   */
  @NotNull(groups = {GroupEdit.class}, message = "组织surrogateId不能为空")
  private Long surrogateId;

  /**
   * 组织名称
   */
  @NotBlank(message = "组织名称不能为空", groups = {GroupAdd.class, GroupEdit.class})
  @Size(min = 2,max = 20, message = "组织名称需要在2到20个字符之间")
  private String name;

  /**
   * 上级组织id
   */
//    private Long parentId;

  /**
   * 上级组织surrogateId
   */
  @NotNull(message = "上级组织parentSurrogateId不能为空", groups = {GroupAdd.class, GroupEdit.class})
  private Long parentSurrogateId;

  /**
   * 排序, 组织咋当前层级目录下的顺序
   */
  @NotNull(message = "组织顺序不能为空", groups = {GroupAdd.class, GroupEdit.class})
  private Integer seq;

  /**
   * 0: 正常, 1: 异常, 2: 未知
   * 新增时默认为0, hard code
   */
  @NotNull(message = "状态不能为空", groups = {GroupAdd.class, GroupEdit.class})
  @Min(value = 0, message = "状态类型范围0~2", groups = {GroupAdd.class, GroupEdit.class})
  @Max(value = 2, message = "状态类型范围0~2", groups = {GroupAdd.class, GroupEdit.class})
  private Integer status;

  /**
   * 备注
   */
  @Size(max = 150,message = "组织备注需要在150个字符以内")
  private String remark;

}
