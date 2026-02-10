package org.cy.micoservice.app.im.facade.dto.router;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: 批量消息推送dto
 */
@Data
public class ImBatchMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1873208662243282134L;

  /**
   * 批量消息推送
   */
  private List<ImSingleMessageDTO> imSingleMessageDTOList;
}