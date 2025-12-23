package org.cy.micoservice.blog.admin.pojo.dto.blog;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogCategory;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryReq;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;

import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.DEFAULT_COLOR;

/**
 * @Author: Lil-K
 * @Date: 2024/4/7
 * @Description:
 */
public class BlogCategoryDTO {
  /**
   * blog save
   * @param req
   * @param blogCategory
   * @return
   */
  public static BlogCategory convertSaveCategoryReq(BlogCategoryReq req, BlogCategory blogCategory) {
    BeanUtils.copyProperties(req, blogCategory);
    blogCategory.setSurrogateId(IdWorker.getSnowFlakeId());

    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());
    blogCategory.setStatus(0);
    blogCategory.setColor(StringUtils.isBlank(req.getColor()) ? DEFAULT_COLOR : req.getColor());
    blogCategory.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
    blogCategory.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    blogCategory.setCreateTime(nowDateTime);
    blogCategory.setUpdateTime(nowDateTime);
    return blogCategory;
  }
}

