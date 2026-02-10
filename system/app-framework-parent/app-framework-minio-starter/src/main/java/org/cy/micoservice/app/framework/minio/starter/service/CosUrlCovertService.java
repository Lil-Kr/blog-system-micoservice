package org.cy.micoservice.app.framework.minio.starter.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: cos存储url转换服务
 */
public interface CosUrlCovertService {

  /**
   * 获取笔记图片url
   * @param noteCosObjectName
   * @return
   */
  String getImgUrl(String noteCosObjectName);

  /**
   * 获取笔记图片集合
   * @param noteCosObjectNames
   * @return
   */
  Map<String,String> batchGetImgUrls(List<String> noteCosObjectNames);

  /**
   * 转换用户头像
   * @param userAvatarUrl
   * @return
   */
  String getUserAvatarUrl(String userAvatarUrl);

  /**
   * 获取用户图片集合
   * @param noteCosObjectNames
   * @return
   */
  Map<String,String> batchGetUserAvatarUrls(List<String> noteCosObjectNames);
}