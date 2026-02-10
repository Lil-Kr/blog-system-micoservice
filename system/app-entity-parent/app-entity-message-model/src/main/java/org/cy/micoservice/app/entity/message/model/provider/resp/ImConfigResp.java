package org.cy.micoservice.app.entity.message.model.provider.resp;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 返回im相关配置信息
 */
@Data
@Builder
public class ImConfigResp {

  private String imServerAddress;

  private String imToken;
}