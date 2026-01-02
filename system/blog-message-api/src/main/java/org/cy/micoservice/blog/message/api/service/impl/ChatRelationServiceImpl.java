package org.cy.micoservice.blog.message.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.base.rpc.RpcResponse;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.common.utils.LocalDateTimeTranslatorUtil;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRelationPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRelationReq;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRelationResp;
import org.cy.micoservice.blog.framework.minio.starter.service.CosUrlCovertService;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.message.api.service.ChatRelationService;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.interfaces.ChatRelationFacade;
import org.cy.micoservice.blog.user.facade.dto.resp.UserRespDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 对话关系service
 */
@Slf4j
@Service
public class ChatRelationServiceImpl implements ChatRelationService {

  @Autowired
  private CosUrlCovertService cosUrlCovertService;

  @DubboReference(check = false)
  private UserFacade userFacade;

  @DubboReference(check = false)
  private ChatRelationFacade chatRelationFacade;

  /**
   * 新增 [聊天会话关系] 信息, 关注 + 打招呼
   * @param chatRelationReq
   * @return
   */
  @Override
  public boolean add(ChatRelationReq chatRelationReq) {
    ChatRelationReqDTO chatRelationReqDTO = BeanCopyUtils.convert(chatRelationReq, ChatRelationReqDTO.class);
    RpcResponse<Boolean> rpcResponse = chatRelationFacade.add(chatRelationReqDTO);
    RpcResponse.isRespSuccess(rpcResponse);
    return rpcResponse.getData();
  }

  /**
   * 分页查询会话关系列表
   * @param chatRelationPageReq
   * @return
   */
  @Override
  public PageResponseDTO<ChatRelationResp> pageChatRelationList(ChatRelationPageReq chatRelationPageReq) {
    // 对话关系查询请求
    ChatRelationPageReqDTO chatRelationPageReqDTO = BeanCopyUtils.convert(chatRelationPageReq, ChatRelationPageReqDTO.class);
    RpcResponse<PageResponseDTO<ChatRelationRespDTO>> rpcResponse = chatRelationFacade.queryInPage(chatRelationPageReqDTO);
    RpcResponse.isRespSuccess(rpcResponse);
    PageResponseDTO<ChatRelationRespDTO> chatRelationPageResp = rpcResponse.getData();
    if (chatRelationPageResp.getDataList().isEmpty()) return PageResponseDTO.emptyPage();
    // Long currentUserId = RequestContext.getUserId();
    // todo: 测试id, 后续删除
    Long currentUserId = chatRelationPageReq.getUserId();

    /**
     * 用户信息请求查询
     */
    List<Long> receiverIds = new ArrayList<>();
    for (ChatRelationRespDTO relation : chatRelationPageResp.getDataList()) {
      Long receiverId = null;
      if (currentUserId.equals(relation.getReceiverId())) {
        receiverId = relation.getUserId();
      } else {
        receiverId = relation.getReceiverId();
      }
      receiverIds.add(receiverId);
    }

    RpcResponse<List<UserRespDTO>> userRpcResp = userFacade.queryInUserIds(receiverIds);
    RpcResponse.isRespSuccess(userRpcResp);

    Map<Long, UserRespDTO> userMap = userRpcResp.getData().stream().collect(Collectors.toMap(UserRespDTO::getUserId, item -> item));
    List<String> avatarUrls = userMap.values().stream().map(UserRespDTO::getAvatar).toList();
    Map<String, String> userAvatarUrlMap = cosUrlCovertService.batchGetUserAvatarUrls(avatarUrls);

    List<ChatRelationResp> chatRelationRespList = new ArrayList<>();
    for (ChatRelationRespDTO relation : chatRelationPageResp.getDataList()) {
      ChatRelationResp chatRelationResp = BeanCopyUtils.convert(relation, ChatRelationResp.class);
      Long relationId = null;
      if (currentUserId.equals(relation.getReceiverId())) {
        relationId = relation.getUserId();
      } else {
        relationId = relation.getReceiverId();
      }
      UserRespDTO receiverInfo = userMap.get(relationId);
      if (receiverInfo != null) {
        chatRelationResp.setObjectAvatar(userAvatarUrlMap.get(receiverInfo.getAvatar()));
        chatRelationResp.setObjectNickname(receiverInfo.getNickname());
      }
      chatRelationResp.setRelationId(relation.getRelationId());
      chatRelationResp.setLatestMsgTime(LocalDateTimeTranslatorUtil.translate(relation.getLatestMsgTime()));
      chatRelationRespList.add(chatRelationResp);
    }
    PageResponseDTO<ChatRelationResp> pageResponseDTO = BeanCopyUtils.convert(chatRelationPageResp, PageResponseDTO.class);
    pageResponseDTO.setDataList(chatRelationRespList);
    return pageResponseDTO;
  }

  @Override
  public void updateRelationByRelationId(ChatRelationReq chatRelationReq) {
    ChatRelationReqDTO chatRelationReqDTO = BeanCopyUtils.convert(chatRelationReq,ChatRelationReqDTO.class);
    RpcResponse<Boolean> rpcResponse = chatRelationFacade.updateRelationByRelationId(chatRelationReqDTO);
    RpcResponse.isRespSuccess(rpcResponse);
  }

  @Override
  public ChatRelationResp getRelationInfo(ChatRelationPageReq chatRelationPageReq) {
    ChatRelationPageReqDTO reqDTO = BeanCopyUtils.convert(chatRelationPageReq,ChatRelationPageReqDTO.class);
    RpcResponse<ChatRelationRespDTO> rpcResponse = chatRelationFacade.queryRelationInfo(reqDTO);
    RpcResponse.isRespSuccess(rpcResponse);
    ChatRelationRespDTO chatRelationRespDTO = rpcResponse.getData();
    ChatRelationResp chatRelationResp = BeanCopyUtils.convert(chatRelationRespDTO, ChatRelationResp.class);

    Long trueReceiverId = null;
    if (RequestContext.getUserId().equals(chatRelationResp.getUserId())) {
      trueReceiverId = chatRelationResp.getReceiverId();
    } else {
      trueReceiverId = chatRelationResp.getUserId();
    }
    List<Long> userIds = new ArrayList<>();
    userIds.add(RequestContext.getUserId());
    userIds.add(trueReceiverId);
    RpcResponse<List<UserRespDTO>> userRpcResp = userFacade.queryInUserIds(userIds);
    RpcResponse.isRespSuccess(userRpcResp);
    List<UserRespDTO> userList = userRpcResp.getData();
    if (CollectionUtils.isEmpty(userList)) {
      return chatRelationResp;
    }
    Map<Long,UserRespDTO> userMap = userList.stream().collect(Collectors.toMap(UserRespDTO::getUserId,item->item));
    UserRespDTO userInfo = userMap.get(RequestContext.getUserId());

    /**
     * 这里需要注意下, 由于 relation 索引里面的 userId 和 receiverId 取决于首次建立关系时候的主动发送方
     * 所以这里需要增加一个判断
     */
    UserRespDTO objectInfo = userMap.get(trueReceiverId);
    chatRelationResp.setUserId(RequestContext.getUserId());
    chatRelationResp.setUserNickname(userInfo.getNickname());
    chatRelationResp.setUserAvatar(cosUrlCovertService.getUserAvatarUrl(userInfo.getAvatar()));
    chatRelationResp.setObjectNickname(objectInfo.getNickname());
    chatRelationResp.setReceiverId(trueReceiverId);
    chatRelationResp.setObjectAvatar(cosUrlCovertService.getUserAvatarUrl(objectInfo.getAvatar()));
    return chatRelationResp;
  }
}
