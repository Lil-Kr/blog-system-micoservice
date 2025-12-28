package org.cy.micoservice.blog.message.api.controller;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.enums.exception.BizErrorEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRelationPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.OpenChatReq;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRecordResp;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRelationResp;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ImConfigResp;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.message.api.service.ChatRecordService;
import org.cy.micoservice.blog.message.api.service.ChatRelationService;
import org.cy.micoservice.blog.message.api.service.ImConfigService;
import org.cy.micoservice.blog.message.api.service.OpenChatService;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: chat controller
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

  @Autowired
  private ChatRelationService chatRelationService;
  @Autowired
  private ChatRecordService chatRecordService;
  @Autowired
  private ImConfigService imConfigService;
  @Autowired
  private OpenChatService openChatService;

  /**
   * 分页查询 用户 -> [会话关系列表]
   * @param chatRelationPageReq
   * @return
   */
  @GetMapping("/relation/pageList")
  public ApiResp<PageResponseDTO<ChatRelationResp>> pageList(ChatRelationPageReq chatRelationPageReq) {
    chatRelationPageReq.setUserId(RequestContext.getUserId());
    return ApiResp.success(chatRelationService.pageList(chatRelationPageReq));
  }

  /**
   * 查询 用户 -> [会话关系信息]
   * @param chatRelationPageReq
   * @return
   */
  @GetMapping("/relation/getRelationInfo")
  public ApiResp<ChatRelationResp> getRelationInfo(ChatRelationPageReq chatRelationPageReq) {
    AssertUtil.isNotNull(chatRelationPageReq, BizErrorEnum.PARAM_ERROR);
    AssertUtil.isNotBlank(chatRelationPageReq.getRelationId(), BizErrorEnum.PARAM_ERROR);
    return ApiResp.success(chatRelationService.getRelationInfo(chatRelationPageReq));
  }

  /**
   * 分页查询 用户 -> [具体聊天信息记录]
   * @param chatRecordPageReq
   * @return
   */
  @GetMapping("/record/pageList")
  public ApiResp<PageResponseDTO<ChatRecordResp>> pageList(ChatRecordPageReq chatRecordPageReq) {
    return ApiResp.success(chatRecordService.pageList(chatRecordPageReq));
  }

  /**
   * 用户发送聊天信息
   * @param chatRecordReq
   * @return
   */
  @PostMapping("/record/sendMsg")
  public ApiResp<Boolean> sendMsg(@RequestBody ChatRecordReq chatRecordReq) {
    AssertUtil.isTrue(chatRecordReq != null && chatRecordReq.getRelationId() != null && chatRecordReq.getContent() != null, BizErrorEnum.PARAM_ERROR);
    chatRecordReq.setUserId(RequestContext.getUserId());
    chatRecordReq.setType(ChatRecordTypeEnum.TEXT.getCode());
    return ApiResp.success(chatRecordService.add(chatRecordReq));
  }

  /**
   * 公共记录用户打开会话窗口的接口
   * @return
   */
  @PostMapping("/open")
  public ApiResp<Boolean> open(@RequestBody OpenChatReq openChatReq) {
    // 这里的并发量可能会非常庞大, 建议用mq来削峰
    AssertUtil.isNotNull(openChatReq, BizErrorEnum.PARAM_ERROR);
    AssertUtil.isNotNull(openChatReq.getRelationId(), BizErrorEnum.PARAM_ERROR);
    AssertUtil.isNotNull(openChatReq.getSeqNo(), BizErrorEnum.PARAM_ERROR);
    openChatReq.setUserId(RequestContext.getUserId());
    return ApiResp.success(openChatService.reportInfo(openChatReq));
  }

  /**
   * 获取im的配置信息
   * @return
   */
  @GetMapping("/getImConfig")
  public ApiResp<ImConfigResp> getImConfig() {
    return ApiResp.success(imConfigService.getImChatConfig(RequestContext.getUserId()));
  }
}