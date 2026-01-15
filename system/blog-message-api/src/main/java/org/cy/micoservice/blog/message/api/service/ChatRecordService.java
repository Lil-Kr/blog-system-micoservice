package org.cy.micoservice.blog.message.api.service;


import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordReq;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRecordResp;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 聊天对话记录 service
 */
public interface ChatRecordService {

  /**
   * 插入发送记录
   * @param req
   * @return
   */
  Boolean add(ChatRecordReq req);

  /**
   * 分页查询聊天记录
   * @param req
   * @return
   */
  PageResponseDTO<ChatRecordResp> pageList(ChatRecordPageReq req);

}
