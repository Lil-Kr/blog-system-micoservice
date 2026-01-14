package org.cy.micoservice.blog.entity.admin.model.req.blog.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.common.constants.CommonConstants;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description:
 */
@ToString
@Data
public class BlogCategoryReq {

  public interface GroupTypeAdd {}
  public interface GroupTypeEdit {}
  public interface GroupTypeDel {}
  public interface GroupTypeDelBatch {}

  @NotNull(groups = {GroupTypeEdit.class, GroupTypeDel.class}, message = "surrogateId is require")
  private Long surrogateId;

  /**
   * batch operation
   */
  @NotBlank(groups = {GroupTypeDelBatch.class}, message = "surrogateId can not be empty")
  private List<Long> surrogateIds;

  @NotBlank(groups = {GroupTypeAdd.class, GroupTypeEdit.class}, message = "number can not be empty")
  private String number;

  @NotBlank(groups = {Default.class, GroupTypeAdd.class}, message = "label name can not be empty")
  @Size(groups = {Default.class, GroupTypeAdd.class, GroupTypeEdit.class}, max = 50, message = "label type name must be within 50 characters.")
  private String name;

  /**
   * 颜色
   */
  @Size(groups = {Default.class, GroupTypeAdd.class, GroupTypeEdit.class}, max = 50, message = "Display color is required, Please enter a valid hexadecimal color code.")
  private String color = CommonConstants.DEFAULT_COLOR;

  @Size(groups = {Default.class, GroupTypeAdd.class, GroupTypeEdit.class}, max = 200, message = "remark must be within 200 characters.")
  private String remark;

  private Integer status;

  private Integer deleted;
}
