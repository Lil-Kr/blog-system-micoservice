package org.cy.micoservice.blog.framework.minio.starter.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.framework.minio.starter.config.MinioConstants;
import org.cy.micoservice.blog.framework.minio.starter.service.CosUrlCovertService;
import org.cy.micoservice.blog.framework.minio.starter.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: MinIO服务实现类
 */
@Service
public class CosUrlCovertServiceImpl implements CosUrlCovertService {

  @Autowired
  private MinioService minIOService;

  @Override
  public String getImgUrl(String noteCosObjectName) {
    String previewUrl = null;
    if (StringUtils.isBlank(noteCosObjectName)) {
      return null;
    }
    try {
      previewUrl = minIOService.getPreSignedObjUrl(MinioConstants.BASE_IMG_PATH + noteCosObjectName, 3600);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return previewUrl;
  }

  @Override
  public Map<String,String> batchGetImgUrls(List<String> noteCosObjectNames) {
    List<String> cosObjectNames = noteCosObjectNames.stream().map(objectName -> MinioConstants.BASE_IMG_PATH + objectName).toList();
    try {
      Map<String,String> resultMap = minIOService.batchGetPreSignedObjUrls(cosObjectNames,3600);
      Map<String,String> convertUrlMap = new HashMap<>();
      for (String cosObjectUrl : resultMap.keySet()) {
        String originObjectName = cosObjectUrl.substring(MinioConstants.BASE_IMG_PATH.length());
        convertUrlMap.put(originObjectName,resultMap.get(cosObjectUrl));
      }
      return convertUrlMap;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getUserAvatarUrl(String userCosObjectName) {
    String previewUrl = null;
    if (StringUtils.isBlank(userCosObjectName)) {
      return null;
    }
    try {
      previewUrl = minIOService.getPreSignedObjUrl(MinioConstants.BASE_AVATAR_PATH + userCosObjectName, 3600);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return previewUrl;
  }

  @Override
  public Map<String, String> batchGetUserAvatarUrls(List<String> userAvatarCosObjectNames) {
    List<String> cosObjectNames = userAvatarCosObjectNames.stream().map(objectName -> MinioConstants.BASE_AVATAR_PATH + objectName).toList();
    try {
      Map<String, String> resultMap = minIOService.batchGetPreSignedObjUrls(cosObjectNames, 3600);
      Map<String, String> convertUrlMap = new HashMap<>();
      for (String cosObjectUrl : resultMap.keySet()) {
        String originObjectName = cosObjectUrl.substring(MinioConstants.BASE_AVATAR_PATH.length());
        convertUrlMap.put(originObjectName, resultMap.get(cosObjectUrl));
      }
      return convertUrlMap;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}