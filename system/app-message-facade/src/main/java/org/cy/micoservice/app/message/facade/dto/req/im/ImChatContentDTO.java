package org.cy.micoservice.app.message.facade.dto.req.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImChatContentDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 5055858022978072644L;

  /**
   * 支持文本/表情/图片等类型数据
   */
  private Integer type;

  /**
   * 真实传递的数据内容信息
   */
  private String body;
}
