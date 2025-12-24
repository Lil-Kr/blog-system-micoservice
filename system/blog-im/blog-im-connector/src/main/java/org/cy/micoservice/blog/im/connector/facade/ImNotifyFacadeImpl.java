package org.cy.micoservice.blog.im.connector.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.common.enums.exception.BizErrorEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.im.connector.service.ImNotifyService;
import org.cy.micoservice.blog.im.facade.connector.dto.ImSingleMessageDTO;
import org.cy.micoservice.blog.im.facade.interfaces.ImNotifyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: im推送通知service
 */
@Slf4j
@Service
@DubboService
public class ImNotifyFacadeImpl implements ImNotifyFacade {

  @Autowired
  private ImNotifyService imNotifyService;

  /**
   * 发送单条消息给指定用户
   * @param singleMessageDTO
   * @return
   */
  @Override
  public RpcResponse<Boolean> sendSingleMessage(ImSingleMessageDTO singleMessageDTO) {
    AssertUtil.isNotNull(singleMessageDTO, BizErrorEnum.PARAM_ERROR);
    AssertUtil.isNotNull(singleMessageDTO.getReceiverId(), BizErrorEnum.PARAM_ERROR);
    AssertUtil.isNotNull(singleMessageDTO.getImMessageDTO(), BizErrorEnum.PARAM_ERROR);
    boolean sendStatus = imNotifyService.sendMsgByUserId(singleMessageDTO.getReceiverId(), singleMessageDTO.getImMessageDTO());
    return RpcResponse.success(sendStatus);
  }
}
