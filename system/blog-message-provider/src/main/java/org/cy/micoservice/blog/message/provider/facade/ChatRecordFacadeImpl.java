package org.cy.micoservice.blog.message.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.rpc.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRecordFacade;
import org.cy.micoservice.blog.message.provider.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Service
@DubboService
public class ChatRecordFacadeImpl implements ChatRecordFacade {

  @Autowired
  private ChatRecordService chatRecordService;

  @Override
  public RpcResponse<Boolean> add(ChatRecordReqDTO chatRecordReqDTO) {
    return RpcResponse.success(chatRecordService.add(chatRecordReqDTO));
  }

  @Override
  public RpcResponse<PageResponseDTO<ChatRecordRespDTO>> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO) {
    return RpcResponse.success(chatRecordService.queryRecordInPage(chatRecordPageReqDTO));
  }
}
