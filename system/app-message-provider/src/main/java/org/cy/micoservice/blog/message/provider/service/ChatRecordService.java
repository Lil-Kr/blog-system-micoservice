package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
public interface ChatRecordService {

  /**
   * 插入发送记录
   * @param chatRecordReqDTO
   * @return
   */
  boolean add(ChatRecordReqDTO chatRecordReqDTO);

  /**
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  PageResponseDTO<ChatRecordRespDTO> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO);
}