package org.cy.micoservice.blog.audit.provider.handler;

import jakarta.annotation.PostConstruct;
import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.enums.AuditPlatformEnum;
import org.cy.micoservice.blog.audit.provider.config.AuditApplicationProperties;
import org.cy.micoservice.blog.audit.provider.handler.impl.image.ALImageAuditHandler;
import org.cy.micoservice.blog.audit.provider.handler.impl.image.ImageAuditHandler;
import org.cy.micoservice.blog.audit.provider.handler.impl.image.WYImageAuditHandler;
import org.cy.micoservice.blog.audit.provider.handler.impl.text.ALTextAuditHandler;
import org.cy.micoservice.blog.audit.provider.handler.impl.text.TextAuditHandler;
import org.cy.micoservice.blog.audit.provider.handler.impl.text.WYTextAuditHandler;
import org.cy.micoservice.blog.common.utils.WeightedRandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审计管理器
 */
@Component
public class AuditManager {

  @Autowired
  private AuditApplicationProperties auditApplicationProperties;
  @Autowired
  private ALTextAuditHandler alTextAuditHandler;
  @Autowired
  private ALImageAuditHandler alImageAuditHandler;
  @Autowired
  private WYTextAuditHandler wyTextAuditHandler;
  @Autowired
  private WYImageAuditHandler wyImageAuditHandler;
  @Autowired
  private ApplicationContext applicationContext;

  private WeightedRandomUtil textWeightedRandomUtil;
  private WeightedRandomUtil imageWeightedRandomUtil;
  private Map<String, Integer> textExpectWeightMap = new ConcurrentHashMap<>();
  private Map<String, Integer> imageExpectWeightMap = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    textWeightedRandomUtil = new WeightedRandomUtil();
    textWeightedRandomUtil.put(AuditPlatformEnum.AL.getCode(), alTextAuditHandler, auditApplicationProperties.getAlTextAuditWeight());
    textWeightedRandomUtil.put(AuditPlatformEnum.WY.getCode(), wyTextAuditHandler, auditApplicationProperties.getWyTextAuditWeight());
    textExpectWeightMap.put(AuditPlatformEnum.AL.getCode(), auditApplicationProperties.getAlTextAuditWeight());
    textExpectWeightMap.put(AuditPlatformEnum.WY.getCode(), auditApplicationProperties.getWyTextAuditWeight());

    imageWeightedRandomUtil = new WeightedRandomUtil();
    imageWeightedRandomUtil.put(AuditPlatformEnum.AL.getCode(), alTextAuditHandler, auditApplicationProperties.getAlImageAuditWeight());
    imageWeightedRandomUtil.put(AuditPlatformEnum.WY.getCode(), wyTextAuditHandler, auditApplicationProperties.getWyImageAuditWeight());
    imageExpectWeightMap.put(AuditPlatformEnum.AL.getCode(), auditApplicationProperties.getAlImageAuditWeight());
    imageExpectWeightMap.put(AuditPlatformEnum.WY.getCode(), auditApplicationProperties.getWyImageAuditWeight());
  }

  private TextAuditHandler getTextAuditHandlerWithWeight() {
    String textAuditTypeKey = textWeightedRandomUtil.selectKey();
    return (TextAuditHandler) textWeightedRandomUtil.get(textAuditTypeKey);
  }

  private ImageAuditHandler getImageAuditHandlerWithWeight() {
    String imageAuditTypeKey = imageWeightedRandomUtil.selectKey();
    return (ImageAuditHandler) imageWeightedRandomUtil.get(imageAuditTypeKey);
  }

  /**
   * 执行文本审核
   * @param auditMsgDTO
   * @return
   */
  public AuditResultDTO doTextAudit(AuditMsgDTO auditMsgDTO) {
    return this.doTextAuditWithRetry(this.getTextAuditHandlerWithWeight(), auditMsgDTO, auditApplicationProperties.getMaxTextAuditRetryTimes());
  }

  /**
   * 执行图片审核
   * @param auditMsgDTO
   * @return
   */
  public AuditResultDTO doImagesAudit(AuditMsgDTO auditMsgDTO) {
    return this.doTextAuditWithRetry(getImageAuditHandlerWithWeight(), auditMsgDTO, auditApplicationProperties.getMaxImageAuditRetryTimes());
  }

  /**
   * 支持重试请求的文本审核处理
   * @param auditMsgDTO
   * @param maxDepth
   * @return
   */
  private AuditResultDTO doTextAuditWithRetry(AuditHandler auditHandler, AuditMsgDTO auditMsgDTO, int maxDepth) {
    AuditResultDTO auditResultDTO = auditHandler.check(auditMsgDTO);
    String channelName = auditHandler.getChannelName();
    Integer expectWeight = textExpectWeightMap.get(channelName);
    Integer currentWeight = textWeightedRandomUtil.getWeight(channelName);
    auditResultDTO.setChannelName(channelName);
    if (!auditResultDTO.isThirdError()) {
      if (expectWeight.equals(currentWeight)) {
        return auditResultDTO;
      }
      // 如果调用成功, 比对当前渠道的权重, 逐渐恢复 (20%概率的恢复系数)
      boolean incrWeight = ThreadLocalRandom.current().nextInt(0, 10) > 8;
      if (incrWeight) {
        textWeightedRandomUtil.incrementWeight(channelName, 2);
      }
      return auditResultDTO;
    }
    // 重试达到上限 则跳出递归
    if (maxDepth <= 0) {
      return AuditResultDTO.thirdError("all channel error");
    }

    // 如果第三方异常, 需要降低该渠道的概率
    textWeightedRandomUtil.decrementWeight(auditHandler.getChannelName(), 2);
    return this.doTextAuditWithRetry(auditHandler,auditMsgDTO, maxDepth - 1);
  }
}