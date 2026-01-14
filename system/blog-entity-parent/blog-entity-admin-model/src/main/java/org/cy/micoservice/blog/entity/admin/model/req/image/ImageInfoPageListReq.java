package org.cy.micoservice.blog.entity.admin.model.req.image;

import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
@Data
public class ImageInfoPageListReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = -1670046865473450975L;

  private Long surrogateId;

  private String name;

  private String imageOriginalName;

  private String imageType;

  private String remark;

  private Long imageCategoryId;
}
