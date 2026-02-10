package org.cy.micoservice.app.entity.message.model.provider.pojo.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.app.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_chat_relation")
@EqualsAndHashCode(callSuper = true)
public class ChatRelation extends BaseEntity {

  @Serial
  private static final long serialVersionUID = -4094699873134825685L;

  // 主键id
  private Long id;

  // 关系id
  private Long relationId;

  // 用户id
  private Long userId;

  // 接收对话的用户id
  private Long receiverId;

  /**
   * 对话内容, 用于展示最后一条记录
   */
  private String content;

  // 对话类型
  private Integer type;

  // 对话状态
  private Integer status;

  // 已对话条数, 前端展示使用
  private Long msgCount;

  // 最新对话时间
  private LocalDateTime latestMsgTime;
}