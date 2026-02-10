package org.cy.micoservice.app.im.router.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.cy.micoservice.app.common.base.provider.RpcResponse;
import org.cy.micoservice.app.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.app.common.utils.AssertUtil;
import org.cy.micoservice.app.im.facade.dto.router.ImSingleMessageDTO;
import org.cy.micoservice.app.im.facade.interfaces.ImNotifyFacade;
import org.cy.micoservice.app.im.router.service.ImPushService;
import org.cy.micoservice.app.im.router.service.UserConnectorMappingService;
import org.cy.micoservice.app.im.router.constant.ImRouterConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@Service
public class ImPushServiceImpl implements ImPushService {

  @DubboReference(check = false)
  private ImNotifyFacade imNotifyFacade;

  @Autowired
  private UserConnectorMappingService userConnectorMappingService;

  @Override
  public void sendSingleMsgToObject(ImSingleMessageDTO imSingleMessageDTO) {
    AssertUtil.isNotNull(imSingleMessageDTO, ApiReturnCodeEnum.PARAMETER_ERROR);
    AssertUtil.isNotNull(imSingleMessageDTO.getImMessageDTO(), ApiReturnCodeEnum.PARAMETER_ERROR);
    AssertUtil.isNotNull(imSingleMessageDTO.getReceiverId(), ApiReturnCodeEnum.PARAMETER_ERROR);
    String address = userConnectorMappingService.getAddressByUserId(imSingleMessageDTO.getReceiverId());
    if (StringUtils.isBlank(address)) {
      // 对方不在线, 需要做离线的消息处理
      return;
    }
    // 设置目标用户当前连接的 im-connector 地址到dubbo上下文
    RpcContext.getServiceContext().setAttachment(ImRouterConstants.IM_ROUTER_DUBBO_CONSTANTS, address);

    // 消息通过rpc推到 im-connector 层
    RpcResponse<Boolean> sendResp = imNotifyFacade.sendSingleMessage(imSingleMessageDTO);
    RpcResponse.isRespSuccess(sendResp);
  }
}
