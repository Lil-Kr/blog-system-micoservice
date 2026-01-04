package org.cy.micoservice.blog.message.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRelationFacade;
import org.cy.micoservice.blog.message.provider.service.ChatRelationEsService;
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
  @Autowired
  private ChatRelationEsService chatRelationEsService;

  /**
   * 新增对话关系
   * @param chatRelationReqDTO
   * @return
   */
  @Override
  public RpcResponse<Boolean> add(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationEsService.add(chatRelationReqDTO));
  }

  /**
   * 分页查询 用户 -> chat-relation 列表
   * @param chatRelationPageReqDTO
   * @return
   */
  @Override
  public RpcResponse<PageResponseDTO<ChatRelationRespDTO>> queryInPage(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    return RpcResponse.success(chatRelationEsService.listChatRelationFromPage(chatRelationPageReqDTO));
  }

  /**
   * 查询对话关系信息
   * @param chatRelationPageReqDTO
   * @return
   */
  @Override
  public RpcResponse<ChatRelationRespDTO> queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    return RpcResponse.success(chatRelationEsService.queryRelationInfo(chatRelationPageReqDTO));
  }

  /**
   * 废弃
   * 更新对话关系信息
   * @param chatRelationReqDTO
   * @return
   */
  @Override
  public RpcResponse<Boolean> updateRelationByRelationId(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationService.updateRelationByRelationId(chatRelationReqDTO));
  }

  /**
   * 废弃
   * 发送对话并且更新对话关系
   * @param chatRelationReqDTO
   * @return
   */
  @Override
  public RpcResponse<Boolean> addRecordAndUpdateRelation(ChatRelationReqDTO chatRelationReqDTO) {
    return RpcResponse.success(chatRelationService.addRecordAndUpdateRelation(chatRelationReqDTO));
  }
}
