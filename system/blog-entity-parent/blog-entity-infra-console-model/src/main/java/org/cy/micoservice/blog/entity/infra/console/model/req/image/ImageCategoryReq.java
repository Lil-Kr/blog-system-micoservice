package org.cy.micoservice.blog.entity.infra.console.model.req.image;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
@Data
public class ImageCategoryReq extends BaseEntity {

  @Serial
  private static final long serialVersionUID = -7098820922267077321L;

  public interface GroupImageCategoryAdd {}
  public interface GroupImageCategoryEdit {}
  public interface GroupImageCategoryDel {}
  public interface GroupImageCategoryDelBatch {}

  @NotNull(groups = {GroupImageCategoryEdit.class, GroupImageCategoryDel.class}, message = "surrogateId is require")
  private Long surrogateId;

  /**
   * batch operation
   */
  @NotNull(groups = {GroupImageCategoryDelBatch.class}, message = "surrogateIds cannot be null")
  private List<Long> surrogateIds;

  @NotNull(groups = {Default.class, GroupImageCategoryAdd.class}, message = "image category name cannot be null")
  @Size(groups = {Default.class, GroupImageCategoryAdd.class, GroupImageCategoryEdit.class}, max = 50, message = "label type name length must be within 50 characters.")
  private String name;

//  @NotNull(groups = {GroupImageCategorySave.class}, message = "标题图url不能为空")
  private String imageUrl;

  private Integer status;

  @Size(groups = {Default.class, GroupImageCategoryAdd.class, GroupImageCategoryEdit.class},max = 200, message = "remark length must be within 200 characters.")
  private String remark;
}
