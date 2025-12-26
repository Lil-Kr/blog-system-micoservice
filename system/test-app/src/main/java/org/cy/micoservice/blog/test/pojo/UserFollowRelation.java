package org.cy.micoservice.blog.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowRelation implements Serializable {
  @Serial
  private static final long serialVersionUID = 6296297447834589955L;

  private Long userId;
  private Long followerId;
  private Long deleted;
  private Date createTime;
  private Date updateTime;
}
