package org.cy.micoservice.app.message.provider.service;

import org.cy.micoservice.app.common.base.provider.PageResponseDTO;
import org.cy.micoservice.app.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.app.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.app.message.facade.dto.resp.ChatRecordRespDTO;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: 消息对话 service 基于ES存储版本
 */
public interface ChatRecordEsService {

  /**
   * im推送过来的数据, 然后进行保存
   * @param imChatReqDTO
   * @return
   */
  boolean index(ImChatReqDTO imChatReqDTO);

  /**
   * 批量保存对话消息
   * @param imChatReqDTOList
   */
  void bulk(List<ImChatReqDTO> imChatReqDTOList);

  /**
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  PageResponseDTO<ChatRecordRespDTO> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO);

  /**
   * 查询未读消息内容
   * @param beginOffset
   * @param endOffset
   * @param relationId
   * @return
   */
  List<ChatRecordRespDTO> queryFromOffset(Long beginOffset, Long endOffset, String relationId);
}