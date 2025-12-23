package org.cy.micoservice.blog.admin.pojo.resp.image;

import org.cy.micoservice.blog.admin.pojo.entity.image.ImageInfo;
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
public class ImageInfoResp extends ImageInfo implements Serializable {
  private static final long serialVersionUID = 5506127010451586144L;

  private String imageCategoryName;
}
