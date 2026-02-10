package org.cy.micoservice.blog.entity.infra.console.model.req.blog.topic;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.cy.micoservice.blog.common.constants.CommonConstants;
import org.cy.micoservice.blog.entity.infra.console.model.req.blog.category.BlogCategoryReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/5/25
 * @Description:
 */
@Data
public class BlogTopicReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = 7698471838797610923L;

  public interface GroupBlogTopicSave {}
  public interface GroupBlogTopicEdit {}
  public interface GroupBlogTopicDeleted {}
  public interface GroupTopicDelBatch {}

  @NotNull(groups = {GroupBlogTopicEdit.class, GroupBlogTopicDeleted.class}, message = "surrogateId is require")
  private Long surrogateId;

  /**
   * batch operation
   */
  @NotNull(groups = {BlogLabelReq.GroupLabelDelBatch.class}, message = "surrogateIds cannot be null")
  private List<Long> surrogateIds;

  @NotNull(groups = {GroupBlogTopicSave.class}, message = "topic number cannot be null")
  private String number;

  @NotNull(groups = {GroupBlogTopicEdit.class, GroupBlogTopicSave.class}, message = "blog topic cannot be null")
  @Size(groups = {GroupBlogTopicEdit.class, GroupBlogTopicSave.class}, max = 50, message = "blog topic length must be within 50 characters.")
  private String name;

  @Size(groups = {Default.class, BlogCategoryReq.GroupTypeAdd.class, BlogCategoryReq.GroupTypeEdit.class}, max = 50, message = " color is required, Please enter a valid hexadecimal color code.")
  private String color = CommonConstants.DEFAULT_COLOR;

  @Size(groups = {Default.class, GroupTopicDelBatch.class}, max = 200, message = "remark length must be within 200 characters.")
  private String remark;

}
