package org.cy.micoservice.app.entity.user.model.provider.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2026/1/19
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollower implements Serializable {
  @Serial
  private static final long serialVersionUID = -1962103341799935748L;

  /**
   * 被关注的人
   */
  private Long userId;

  /**
   * 粉丝id
   */
  private Long followerId;

  private Long createTime;

  private Long updateTime;

  private Long deleted;
}
