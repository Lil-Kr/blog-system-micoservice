package org.cy.micoservice.blog.audit.provider.handler.impl.text;

import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.dto.text.TextAuditBody;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum;
import org.cy.micoservice.blog.audit.provider.handler.AuditHandler;
import org.cy.micoservice.blog.common.exception.BizException;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 文本内容审核处理器
 */
@Component
public abstract class TextAuditHandler implements AuditHandler {

//  @Autowired
//  private UserBlackListMapper userBlackListMapper;

  @Override
  public AuditResultDTO check(AuditMsgDTO auditMsgDTO) {
    Integer auditType = auditMsgDTO.getAuditType();
    if (! AuditTypeEnum.TEXT.getCode().equals(auditType)) {
      throw new BizException("审核类型不匹配异常");
    }

    TextAuditBody textAuditBody = JSON.parseObject(auditMsgDTO.getAuditBody(), TextAuditBody.class);
    Integer bodyType = textAuditBody.getBodyType();
    if (TextAuditBodyTypeEnum.CHAT.getCode().equals(bodyType)) {
      // 会话消息审核处理逻辑
      AuditResultDTO auditResultDTO = checkContentBody(textAuditBody.getBody());
      if (!auditResultDTO.getSuccess()) {
        return auditResultDTO;
      }
    }
    return AuditResultDTO.pass();
  }

  public abstract AuditResultDTO checkContentBody(String content);

  /**
   * 获取渠道名称
   *
   * @return
   */
  public abstract String getChannelName();

//  /**
//   * 审核笔记内容
//   *
//   * @param noteRespDTO
//   */
//  private AuditResultDTO checkNoteContent(NoteRespDTO noteRespDTO) {
//    //内容审核
//    String textContent = noteRespDTO.getContent();
//    return checkContentBody(textContent);
//  }

  /**
   * 用户id是否合法检测
   * @param userId
   * @return
   */
  private AuditResultDTO checkUserIsValid(Long userId) {
    return null;
  }
}