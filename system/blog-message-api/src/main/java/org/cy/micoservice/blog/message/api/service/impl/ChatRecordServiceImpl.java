package org.cy.micoservice.blog.message.api.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRecordReq;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRecordResp;
import org.cy.micoservice.blog.framework.minio.starter.service.CosUrlCovertService;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.message.api.service.ChatRecordService;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordSendTypeEnum;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRecordFacade;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRelationFacade;
import org.cy.micoservice.blog.user.facade.dto.resp.UserRespDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.cy.micoservice.blog.common.enums.exception.BizErrorEnum.PARAM_ERROR;
import static org.cy.micoservice.blog.common.enums.exception.BizErrorEnum.SYSTEM_ERROR;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 聊天记录service
 */
@Slf4j
@Service
public class ChatRecordServiceImpl implements ChatRecordService {

  @DubboReference(check = false)
  private ChatRecordFacade chatRecordFacade;
  @DubboReference(check = false)
  private ChatRelationFacade chatRelationFacade;
  @DubboReference(check = false)
  private UserFacade userFacade;
  @Autowired
  private CosUrlCovertService cosUrlCovertService;

  /**
   * 插入发送记录
   * @param req
   * @return
   */
  @Override
  public Boolean add(ChatRecordReq req) {
    AssertUtil.isNotNull(req.getReceiverId(), PARAM_ERROR);
    ChatRelationReqDTO chatRelationReqDTO = BeanCopyUtils.convert(req, ChatRelationReqDTO.class);
    chatRelationReqDTO.setUserId(RequestContext.getUserId());
    RpcResponse<Boolean> addStatus = chatRelationFacade.addRecordAndUpdateRelation(chatRelationReqDTO);
    return addStatus.isSuccess() && addStatus.getData();
  }

  /**
   * 分页查询聊天记录
   * @param req
   * @return
   */
  @Override
  public PageResponseDTO<ChatRecordResp> pageList(ChatRecordPageReq req) {
    AssertUtil.isNotNull(req.getRelationId(), PARAM_ERROR);
    Long currentUserId = req.getUserId();

    ChatRecordPageReqDTO reqDTO = BeanCopyUtils.convert(req, ChatRecordPageReqDTO.class);
    reqDTO.setRelationId(req.getRelationId());
    reqDTO.setSearchOffset(req.getSearchOffset());
    RpcResponse<PageResponseDTO<ChatRecordRespDTO>> resp = chatRecordFacade.queryRecordInPage(reqDTO);
    AssertUtil.isTrue(resp.isSuccess(), SYSTEM_ERROR);
    PageResponseDTO<ChatRecordRespDTO> pageResponseDTO = resp.getData();
    List<ChatRecordRespDTO> recordList = pageResponseDTO.getDataList();
    if (CollectionUtils.isEmpty(recordList)) return PageResponseDTO.emptyPage();

    List<Long> userIdList = recordList.stream().map(ChatRecordRespDTO::getUserId).toList();
    List<Long> receiverIdList = recordList.stream().map(ChatRecordRespDTO::getReceiverId).toList();
    List<Long> mergeIdList = new ArrayList<>();
    mergeIdList.addAll(userIdList);
    mergeIdList.addAll(receiverIdList);
    RpcResponse<List<UserRespDTO>> userRpcResp = userFacade.queryInUserIds(mergeIdList);
    AssertUtil.isTrue(userRpcResp.isSuccess(), SYSTEM_ERROR);
    if (CollectionUtils.isEmpty(userRpcResp.getData())) return PageResponseDTO.emptyPage();

    Map<Long, UserRespDTO> userMap = userRpcResp.getData().stream().collect(Collectors.toMap(UserRespDTO::getUserId, item -> item));
    List<ChatRecordResp> chatRecordRespList = Lists.newArrayListWithExpectedSize(recordList.size());
    for (ChatRecordRespDTO chatRecord : recordList) {
      ChatRecordResp chatRecordResp = new ChatRecordResp();
      if (currentUserId.equals(chatRecord.getUserId())) {
        chatRecordResp.setType(ChatRecordSendTypeEnum.SENT.getCode());
      } else {
        chatRecordResp.setType(ChatRecordSendTypeEnum.RECEIVER.getCode());
      }
      UserRespDTO sender = userMap.get(chatRecord.getUserId());
      if (sender != null) {
        chatRecordResp.setAvatar(cosUrlCovertService.getUserAvatarUrl(sender.getAvatar()));
        chatRecordResp.setUserId(sender.getUserId());
      }
      chatRecordResp.setSeqNo(chatRecord.getSeqNo());
      chatRecordResp.setContent(chatRecord.getContent());
      chatRecordRespList.add(chatRecordResp);
    }
    PageResponseDTO<ChatRecordResp> pageResponse = BeanCopyUtils.convert(pageResponseDTO, PageResponseDTO.class);
    pageResponse.setDataList(chatRecordRespList);
    return pageResponse;
  }
}
