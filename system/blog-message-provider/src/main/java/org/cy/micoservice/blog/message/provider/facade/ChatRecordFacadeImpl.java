package org.cy.micoservice.blog.message.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRecordFacade;
import org.cy.micoservice.blog.message.provider.service.ChatRecordEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 消息记录
 */
@Service
@DubboService
public class ChatRecordFacadeImpl implements ChatRecordFacade {

  @Autowired
  private ChatRecordEsService chatRecordEsService;

  /**
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  @Override
  public RpcResponse<PageResponseDTO<ChatRecordRespDTO>> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO) {
    return RpcResponse.success(chatRecordEsService.queryRecordInPage(chatRecordPageReqDTO));
  }
}
