package org.cy.micoservice.blog.entity.admin.model.resp.blog;

import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogDiary;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
@ToString
@Data
public class BlogDiaryPageListResp extends BlogDiary {

  @Serial
  private static final long serialVersionUID = -3100079751347543684L;
  
  private String creatorName;
  private String operatorName;
}
