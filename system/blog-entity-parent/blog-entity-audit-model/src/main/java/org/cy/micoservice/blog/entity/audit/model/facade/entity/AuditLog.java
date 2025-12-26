package org.cy.micoservice.blog.entity.audit.model.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审计日志PO 建议做日期的归档 -》当前一个月走热表存储, 后续一个月前的数据做冷备份
 * 建议: 历史归档数据走离线存储
 */
@TableName("t_audit_log")
@Data
public class AuditLog implements Serializable {

  @Serial
  private static final long serialVersionUID = -1140480818028591199L;

  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 事件时间
   */
  private Long eventTime;

  /**
   * 审记结果code
   */
  private Integer resultCode;

  /**
   * 审记结果消息内容
   */
  private String message;

  /**
   * 审记渠道
   * @see org.cy.micoservice.blog.entity.audit.model.facade.enums.AuditPlatformEnum
   */
  private String channel;

  /**
   * 审计记录第三方关联id
   */
  private String refId;

  /**
   * 第三方数据类型
   * @see org.cy.micoservice.blog.entity.audit.model.facade.enums.AuditRefTypeEnum
   */
  private Integer refType;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;


  @TableLogic(delval = "id",value = "0")
  private Long deleted;
}