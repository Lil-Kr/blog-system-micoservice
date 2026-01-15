package org.cy.micoservice.blog.entity.infra.console.model.req.blog.content;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Lil-K
 * @Date: 2025/4/22
 * @Description:
 */
@Data
@ToString
public class BlogRichEditorImageReq {

  private MultipartFile image;

  private Long blogId;
}