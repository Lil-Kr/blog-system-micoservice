package org.cy.micoservice.blog.admin.pojo.resp.blog;

import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogContent;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/5/24
 * @Description:
 */
@ToString
@Data
public class BlogContentResp extends BlogContent implements Serializable {

  private static final long serialVersionUID = -6216586260975821759L;

  private String contentText;
  /**
   * label
   */
  private List<BlogLabel> blogLabelList;

  private String categoryName;

  private String categoryColor;

  private String topicName;

  private String topicColor;

  private Integer originalType;

  private Integer recommendType;

  private Integer statusType;

  private String statusName;

  private PrevAndNext prev;

  private PrevAndNext next;
}
