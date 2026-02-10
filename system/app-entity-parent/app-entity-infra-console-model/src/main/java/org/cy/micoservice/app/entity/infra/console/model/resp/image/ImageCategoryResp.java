package org.cy.micoservice.app.entity.infra.console.model.resp.image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.entity.image.ImageCategory;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class ImageCategoryResp extends ImageCategory implements Serializable {

  @Serial
  private static final long serialVersionUID = -575350097845168902L;

  private PageResult<ImageInfoResp> imageInfo;
}
