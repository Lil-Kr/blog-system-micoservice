package org.cy.micoservice.blog.message.facade.interfaces;

import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
public interface ChatRecordFacade {

  /**
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  RpcResponse<PageResponseDTO<ChatRecordRespDTO>> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO);
}