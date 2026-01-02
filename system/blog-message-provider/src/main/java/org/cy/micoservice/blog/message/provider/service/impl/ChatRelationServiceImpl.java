package org.cy.micoservice.blog.message.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.common.utils.IdGenerateUtil;
import org.cy.micoservice.blog.entity.message.model.provider.po.mysql.ChatRelation;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordEnum;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordStatusEnum;
import org.cy.micoservice.blog.message.facade.enums.ChatRelationStatusEnum;
import org.cy.micoservice.blog.message.provider.dao.mysql.ChatRelationMapper;
import org.cy.micoservice.blog.message.provider.service.ChatRecordService;
import org.cy.micoservice.blog.message.provider.service.ChatRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系service
 */
@Service
public class ChatRelationServiceImpl extends ServiceImpl<ChatRelationMapper, ChatRelation> implements ChatRelationService {

  @Autowired
  private ChatRecordService chatRecordService;

  /**
   * 新增会话关系
   * @param chatRelationReqDTO
   * @return
   */
  @Override
  public boolean add(ChatRelationReqDTO chatRelationReqDTO) {
    Long relationId = IdGenerateUtil.generateRelationId();
    ChatRelation userChatRelation = BeanCopyUtils.convert(chatRelationReqDTO, ChatRelation.class);
    userChatRelation.setRelationId(relationId);
    userChatRelation.setStatus(ChatRelationStatusEnum.VALID.getCode());

    ChatRelation receiverChatRelation = BeanCopyUtils.convert(chatRelationReqDTO, ChatRelation.class);
    receiverChatRelation.setUserId(chatRelationReqDTO.getReceiverId());
    receiverChatRelation.setReceiverId(chatRelationReqDTO.getUserId());
    receiverChatRelation.setRelationId(relationId);
    receiverChatRelation.setStatus(ChatRelationStatusEnum.VALID.getCode());

    try {
      List<ChatRelation> chatRelationList = new ArrayList<>();
      chatRelationList.add(userChatRelation);
      chatRelationList.add(receiverChatRelation);
      super.saveBatch(chatRelationList);
    } catch (DuplicateKeyException e){
      // 有可能之前以及有过会话关系
    }

    if (chatRelationReqDTO.getContent() != null) {
      ChatRecordReqDTO chatRecordReqDTO = new ChatRecordReqDTO();
      chatRecordReqDTO.setUserId(chatRelationReqDTO.getReceiverId());
      chatRecordReqDTO.setReceiverId(chatRelationReqDTO.getUserId());
      // chatRecordReqDTO.setRelationId(relationId);
      chatRecordReqDTO.setContent(chatRelationReqDTO.getContent());
      chatRecordReqDTO.setType(ChatRecordEnum.TEXT.getCode());
      chatRecordService.add(chatRecordReqDTO);
    }
    return true;
  }

  @Override
  public PageResponseDTO<ChatRelationRespDTO> queryInPage(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    IPage<ChatRelation> chatRelationIPage = new Page<>(chatRelationPageReqDTO.getCurrentPageNum(), chatRelationPageReqDTO.getPageSize());
    LambdaQueryWrapper<ChatRelation> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatRelation::getUserId, chatRelationPageReqDTO.getUserId());
    queryWrapper.orderByDesc(ChatRelation::getLatestMsgTime);
    IPage<ChatRelation> chatRelationPage = super.getBaseMapper().selectPage(chatRelationIPage, queryWrapper);
    if (chatRelationPage.getRecords().isEmpty()) {
      return PageResponseDTO.emptyPage();
    }
    List<ChatRelationRespDTO> chatRelationRespDTOList = BeanCopyUtils.convertList(chatRelationPage.getRecords(), ChatRelationRespDTO.class);
    PageResponseDTO<ChatRelationRespDTO> pageResponseDTO = new PageResponseDTO<>();
    pageResponseDTO.setHasNext(chatRelationPage.getTotal() > (chatRelationPage.getPages() * chatRelationPage.getSize()));
    pageResponseDTO.setPage(chatRelationPageReqDTO.getCurrentPageNum());
    pageResponseDTO.setSize(chatRelationPageReqDTO.getPageSize());
    pageResponseDTO.setDataList(chatRelationRespDTOList);
    return pageResponseDTO;
  }

  @Override
  public boolean updateRelationByRelationId(ChatRelationReqDTO chatRelationReqDTO) {
    // this.getBaseMapper().updateRelationByRelationId(chatRelationReqDTO.getRelationId(), chatRelationReqDTO.getContent());
    return true;
  }

  @Override
  public ChatRelationRespDTO queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO) {
    String relationId = chatRelationPageReqDTO.getRelationId();
    LambdaQueryWrapper<ChatRelation> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatRelation::getRelationId, relationId);
    queryWrapper.last("limit 1");
    ChatRelation chatRelation = super.getBaseMapper().selectOne(queryWrapper);
    ChatRelationRespDTO chatRelationRespDTO = BeanCopyUtils.convert(chatRelation, ChatRelationRespDTO.class);

    List<Long> userIds = new ArrayList<>();
    userIds.add(chatRelation.getUserId());
    userIds.add(chatRelation.getReceiverId());
    return chatRelationRespDTO;
  }

  @Override
  public boolean addRecordAndUpdateRelation(ChatRelationReqDTO chatRelationReqDTO) {
    ChatRecordReqDTO chatRecordReqDTO = BeanCopyUtils.convert(chatRelationReqDTO, ChatRecordReqDTO.class);
    chatRecordReqDTO.setChatId(IdGenerateUtil.generateChatRecordId());
    chatRecordReqDTO.setStatus(ChatRecordStatusEnum.VALID.getCode());
    return chatRecordService.add(chatRecordReqDTO) && this.updateRelationByRelationId(chatRelationReqDTO);
  }
}
