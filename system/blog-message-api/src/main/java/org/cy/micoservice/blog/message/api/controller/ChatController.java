package org.cy.micoservice.blog.message.api.controller;

import jakarta.validation.Valid;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.enums.exception.BizErrorEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.*;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRecordResp;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRelationResp;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ImConfigResp;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.message.api.service.ChatRecordService;
import org.cy.micoservice.blog.message.api.service.ChatRelationService;
import org.cy.micoservice.blog.message.api.service.ImConfigService;
import org.cy.micoservice.blog.message.api.service.OpenChatService;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
   * 新增 [聊天会话关系] 信息
   * @param chatRelationReq
   * @return
   */
  @NoAuthCheck
  @PostMapping("/relation/add")
  public ApiResp<Boolean> addChatRelation(@RequestBody @Valid ChatRelationReq chatRelationReq) {
    return ApiResp.success(chatRelationService.add(chatRelationReq));
  }

  /**
   * 分页查询 用户 -> [会话关系列表]
   * 用户切换到会话列表, 从聊天窗口后退至会话列表, 这两种情况需要调用这个api进行更新
   * @param chatRelationPageReq
   * @return
   */
  @NoAuthCheck
  @PostMapping("/relation/pageList")
  public ApiResp<PageResponseDTO<ChatRelationResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class, ChatRelationPageReq.GroupPageQuery.class}) ChatRelationPageReq chatRelationPageReq) {
    // todo: 测试id, 后续删除, 用上面注释的代码
    // chatRelationPageReq.setUserId(RequestContext.getUserId());
    chatRelationPageReq.setUserId(chatRelationPageReq.getUserId());
    return ApiResp.success(chatRelationService.pageChatRelationList(chatRelationPageReq));
  }

  /**
   * 查询单个 用户 -> [会话关系信息]
   * @param chatRelationPageReq
   * @return
   */
  @NoAuthCheck
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
  @NoAuthCheck
  @PostMapping("/record/pageList")
  public ApiResp<PageResponseDTO<ChatRecordResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) ChatRecordPageReq chatRecordPageReq) {
    return ApiResp.success(chatRecordService.pageList(chatRecordPageReq));
  }

  /**
   * 用户发送聊天信息
   * @param req
   * @return
   */
  @NoAuthCheck
  @PostMapping("/record/sendMsg")
  public ApiResp<Boolean> sendMsg(@RequestBody ChatRecordReq req) {
    AssertUtil.isTrue(req != null && req.getRelationId() != null && req.getContent() != null, BizErrorEnum.PARAM_ERROR);
    req.setUserId(RequestContext.getUserId());
    req.setType(ChatRecordTypeEnum.TEXT.getCode());
    return ApiResp.success(chatRecordService.add(req));
  }

  /**
   * todo: 后续需增加防止恶意攻击的处理
   * 用户打开会话窗口: 查看公共聊天记录, 上报已读offset点位
   * 这个api 并发量会很大, 使用MQ做削峰
   * @return
   */
  @NoAuthCheck
  @PostMapping("/open")
  public ApiResp<Boolean> open(@RequestBody @Valid OpenChatReq openChatReq) {
    /**
     * 这里的并发量可能会非常庞大, 建议用mq来削峰
     */
    // openChatReq.setUserId(RequestContext.getUserId());
    return ApiResp.success(openChatService.reportInfo(openChatReq));
  }

  /**
   * 获取im的配置信息
   * @return
   */
  @NoAuthCheck
  @GetMapping("/getImConfig")
  public ApiResp<ImConfigResp> getImConfig() {
    return ApiResp.success(imConfigService.getImChatConfig(RequestContext.getUserId()));
  }
}