package org.cy.micoservice.blog.admin.pojo.req.blog.diary;

import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
@ToString
@Data
public class DiaryPageListReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = -5291758549157824185L;
}