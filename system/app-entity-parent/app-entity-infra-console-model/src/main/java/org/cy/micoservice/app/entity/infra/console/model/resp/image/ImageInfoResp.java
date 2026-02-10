package org.cy.micoservice.app.entity.infra.console.model.resp.image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.entity.infra.console.model.entity.image.ImageInfo;

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
public class ImageInfoResp extends ImageInfo implements Serializable {

  @Serial
  private static final long serialVersionUID = -8360009511210616521L;

  private String imageCategoryName;
}
