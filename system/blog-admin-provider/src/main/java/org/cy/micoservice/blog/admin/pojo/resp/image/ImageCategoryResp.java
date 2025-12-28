package org.cy.micoservice.blog.admin.pojo.resp.image;

import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.admin.pojo.entity.image.ImageCategory;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
@ToString
@Data
public class ImageCategoryResp extends ImageCategory implements Serializable {
  private static final long serialVersionUID = -3041539406496075737L;

  private PageResult<ImageInfoResp> imageInfo;
}
