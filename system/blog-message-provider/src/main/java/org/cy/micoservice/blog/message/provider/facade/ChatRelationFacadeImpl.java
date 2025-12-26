package org.cy.micoservice.blog.message.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRelationFacade;
import org.cy.micoservice.blog.message.provider.service.ChatRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Service
@DubboService
public class ChatRelationFacadeImpl implements ChatRelationFacade {

  @Autowired
  private ChatRelationService chatRelationService;

  @Override
  public RpcResponse<Boolean> add(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationService.add(chatRelationReqDTO));
  }

  @Override
  public RpcResponse<PageResponse<ChatRelationRespDTO>> queryInPage(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    return RpcResponse.success(chatRelationService.queryInPage(chatRelationPageReqDTO));
  }

  @Override
  public RpcResponse<Boolean> updateRelationByRelationId(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationService.updateRelationByRelationId(chatRelationReqDTO));
  }

  @Override
  public RpcResponse<ChatRelationRespDTO> queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    return RpcResponse.success(chatRelationService.queryRelationInfo(chatRelationPageReqDTO));
  }

  @Override
  public RpcResponse<Boolean> addRecordAndUpdateRelation(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationService.addRecordAndUpdateRelation(chatRelationReqDTO));
  }
}
