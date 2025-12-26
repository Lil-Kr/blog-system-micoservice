package org.cy.micoservice.blog.message.facade.interfaces;

import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
public interface ChatRecordFacade {

  /**
   * 插入发送记录
   * @param chatRecordReqDTO
   * @return
   */
  RpcResponse<Boolean> add(ChatRecordReqDTO chatRecordReqDTO);

  /**
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  RpcResponse<PageResponse<ChatRecordRespDTO>> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO);
}