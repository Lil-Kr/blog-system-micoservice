package org.cy.micoservice.blog.im.facade.router.connector.dto.body;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: im 服务登录消息体
 */
@Data
public class ImLoginBody implements Serializable {

  @Serial
  private static final long serialVersionUID = -5284523659902433513L;

  /**
   * 当前登录的用户id
   */
  private Long userId;

  /**
   * jwt 认证 token 信息
   */
  private String token;

  /**
   * 认证状态
   */
  private short status;

  /**
   * IM 连接机器地址
   */
  private String ImConnectorAddress;

  /**
   * 业务归属uri, 自定义, 灵活使用
   */
  private String uri;

}
