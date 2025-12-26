package org.cy.micoservice.blog.message.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cy.micoservice.blog.common.base.PageResponse;
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
import org.springframework.util.CollectionUtils;

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
    Long relationId = chatRecordReqDTO.getRelationId();
    ChatRelationReqDTO chatRelationReqDTO = new ChatRelationReqDTO();
    chatRelationReqDTO.setContent(chatRecordReqDTO.getContent());
    chatRelationReqDTO.setRelationId(relationId);
    chatRelationService.updateRelationByRelationId(chatRelationReqDTO);
    ChatRecord chatRecordPO = BeanCopyUtils.convert(chatRecordReqDTO, ChatRecord.class);
    chatRecordPO.setChatId(IdGenerateUtil.generateChatRecordId());
    chatRecordPO.setStatus(ChatRecordStatusEnum.VALID.getCode());
    return super.getBaseMapper().insert(chatRecordPO) > 0;
  }

  /**
   * todo: 改为自定义分页, 不使用MP的
   * 分页查询聊天记录
   * @param chatRecordPageReqDTO
   * @return
   */
  @Override
  public PageResponse<ChatRecordRespDTO> queryRecordInPage(ChatRecordPageReqDTO chatRecordPageReqDTO) {
    if (chatRecordPageReqDTO.getRelationId() == null) {
      return PageResponse.emptyPage();
    }
    IPage<ChatRecord> page = new Page<>(chatRecordPageReqDTO.getCurrentPageNum(),chatRecordPageReqDTO.getPageSize());
    LambdaQueryWrapper<ChatRecord> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatRecord::getRelationId, chatRecordPageReqDTO.getRelationId());
    queryWrapper.orderByAsc(ChatRecord::getId);
    IPage<ChatRecord> chatRecordPOPage  = super.getBaseMapper().selectPage(page,queryWrapper);
    List<ChatRecord> recordPOList = chatRecordPOPage.getRecords();
    if (CollectionUtils.isEmpty(recordPOList)) {
      return PageResponse.emptyPage();
    }
    List<ChatRecordRespDTO> chatRecordRespDTOS = BeanCopyUtils.convertList(recordPOList, ChatRecordRespDTO.class);
    PageResponse<ChatRecordRespDTO> pageResponseVO = new PageResponse<>();
    pageResponseVO.setDataList(chatRecordRespDTOS);
    pageResponseVO.setPage(chatRecordPageReqDTO.getCurrentPageNum());
    pageResponseVO.setSize(chatRecordPageReqDTO.getPageSize());
    pageResponseVO.setHasNext(chatRecordPOPage.getTotal() > (chatRecordPOPage.getPages() * chatRecordPOPage.getSize()));
    return pageResponseVO;
  }
}
