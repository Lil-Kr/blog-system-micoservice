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
   * get note image url
   * @param noteCosObjectName
   * @return
   */
  String getImgUrl(String noteCosObjectName);

  /**
   * get note image collection
   * @param noteCosObjectNames
   * @return
   */
  Map<String, String> batchGetImgUrls(List<String> noteCosObjectNames);

  /**
   * transform user header image
   * @param userAvatarUrl
   * @return
   */
  String getUserAvatarUrl(String userAvatarUrl);

  /**
   * get user image collection
   * @param noteCosObjectNames
   * @return
   */
  Map<String, String> batchGetUserAvatarUrls(List<String> noteCosObjectNames);
}