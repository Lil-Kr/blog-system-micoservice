package org.cy.micoservice.blog.im.facade.dto.connector.body;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/14
 * @Description: 发送业务消息响应 body
 */
@Data
@Builder
public class ImBizMsgResp implements Serializable {

  @Serial
  private static final long serialVersionUID = 714605335571125252L;

  private short status;

  private String traceId;

}
