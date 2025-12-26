package org.cy.micoservice.blog.entity.message.model.provider.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.BaseEntity;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@TableName("t_chat_record")
public class ChatRecord extends BaseEntity {
  @Serial
  private static final long serialVersionUID = -3660330681250827415L;

  // 主键id
  private Long id;

  // 关系id
  private Long relationId;

  // 对话id
  private Long chatId;

  // 用户id
  private Long userId;

  // 接收对话的用户id
  private Long objectId;

  // 对话内容
  private String content;

  // 对话类型
  private Integer type;

  // 对话状态
  private Integer status;

  // 对话序号
  private Integer seqNo;
}
