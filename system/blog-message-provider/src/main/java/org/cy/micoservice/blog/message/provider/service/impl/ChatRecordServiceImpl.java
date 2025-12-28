package org.cy.micoservice.blog.message.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.common.utils.IdGenerateUtil;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatRecord;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRecordReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatRecordStatusEnum;
import org.cy.micoservice.blog.message.provider.dao.mysql.ChatRecordMapper;
import org.cy.micoservice.blog.message.provider.service.ChatRecordService;
import org.cy.micoservice.blog.message.provider.service.ChatRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 聊天记录service
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {

  @Lazy
  @Autowired
  private ChatRelationService chatRelationService;

  @Override
  public boolean add(ChatRecordReqDTO chatRecordReqDTO) {
    // Long relationId = chatRecordReqDTO.getRelationId();
    ChatRelationReqDTO chatRelationReqDTO = new ChatRelationReqDTO();
    chatRelationReqDTO.setContent(chatRecordReqDTO.getContent());
    // chatRelationReqDTO.setRelationId(relationId);
    chatRelationService.updateRelationByRelationId(chatRelationReqDTO);
    ChatRecord chatRecord = BeanCopyUtils.convert(chatRecordReqDTO, ChatRecord.class);
    chatRecord.setChatId(IdGenerateUtil.generateChatRecordId());
    chatRecord.setStatus(ChatRecordStatusEnum.VALID.getCode());
    return super.getBaseMapper().insert(chatRecord) > 0;
  }

  /**
   * todo: 改为自定义分页, 不使用MP的
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  @Override
  public PageResponseDTO<ChatRecordRespDTO> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO) {
    if (chatRecordPageReqDTO.getRelationId() == null) {
      return PageResponseDTO.emptyPage();
    }
    IPage<ChatRecord> page = new Page<>(chatRecordPageReqDTO.getCurrentPageNum(),chatRecordPageReqDTO.getPageSize());
    LambdaQueryWrapper<ChatRecord> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatRecord::getRelationId, chatRecordPageReqDTO.getRelationId());
    queryWrapper.orderByAsc(ChatRecord::getId);
    IPage<ChatRecord> chatRecordPage  = super.getBaseMapper().selectPage(page,queryWrapper);
    List<ChatRecord> recordList = chatRecordPage.getRecords();
    if (CollectionUtils.isEmpty(recordList)) {
      return PageResponseDTO.emptyPage();
    }
    List<ChatRecordRespDTO> chatRecordRespDTOS = BeanCopyUtils.convertList(recordList, ChatRecordRespDTO.class);
    PageResponseDTO<ChatRecordRespDTO> pageResponseDTO = new PageResponseDTO<>();
    pageResponseDTO.setDataList(chatRecordRespDTOS);
    pageResponseDTO.setPage(chatRecordPageReqDTO.getCurrentPageNum());
    pageResponseDTO.setSize(chatRecordPageReqDTO.getPageSize());
    pageResponseDTO.setHasNext(chatRecordPage.getTotal() > (chatRecordPage.getPages() * chatRecordPage.getSize()));
    return pageResponseDTO;
  }
}
