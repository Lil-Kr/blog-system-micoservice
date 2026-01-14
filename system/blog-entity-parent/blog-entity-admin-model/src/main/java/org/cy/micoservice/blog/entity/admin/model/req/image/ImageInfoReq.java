package org.cy.micoservice.blog.entity.admin.model.req.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
@ToString
@Data
public class ImageInfoReq {

  public interface GroupImageInfoAdd {}
  public interface GroupImageInfoEdit {}
  public interface GroupImageInfoDel {}
  public interface GroupImageInfoDelBatch {}

  @NotNull(groups = {GroupImageInfoEdit.class, GroupImageInfoDel.class}, message = "surrogateId is require")
  private Long surrogateId;

  /**
   * batch operation
   */
  @NotNull(groups = {GroupImageInfoDelBatch.class}, message = "surrogateIds cannot be null")
  private List<Long> surrogateIds;

  @NotNull(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, message = "imageCategoryId cannot be null")
  private Long imageCategoryId;

  @NotBlank(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, message = "number cannot be empty")
  private String number;

  @NotBlank(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, message = "image name cannot be empty")
  @Size(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, max = 50, message = "image name length must be within 50 characters.")
  private String name;

  @NotBlank(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, message = "imageOriginalName cannot be empty")
  @Size(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, max = 50, message = "imageOriginalName length must be within 50 characters.")
  private String imageOriginalName;

  @NotBlank(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, message = "image type cannot be empty")
  private String imageType;

  @Size(groups = {GroupImageInfoAdd.class, GroupImageInfoEdit.class}, max = 200, message = "remark length must be within 200 characters.")
  private String remark;
}
