package org.cy.micoservice.blog.framework.minio.starter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description:
 */
public interface MinioService {

  /**
   * 上传文件
   *
   * @param file
   * @param objectName
   * @throws Exception
   */
  void uploadFile(MultipartFile file, String objectName) throws Exception;

  /**
   * 下载文件
   *
   * @param objectName
   * @return
   * @throws Exception
   */
  InputStream downloadFile(String objectName) throws Exception;

  /**
   * 删除文件
   *
   * @param objectName
   * @throws Exception
   */
  void deleteFile(String objectName) throws Exception;

  /**
   * 文件是否存在
   *
   * @param objectName
   * @return
   * @throws Exception
   */
  boolean isFileExists(String objectName) throws Exception;

  /**
   * 获取文件资源的临时链接
   *
   * @param objectName
   * @param expireSecond
   * @return
   * @throws Exception
   */
  String getPreSignedObjUrl(String objectName, int expireSecond) throws Exception;

  /**
   * 批量换取文件资源的临时链接
   *
   * @param objectNames
   * @return
   * @throws Exception
   */
  Map<String,String> batchGetPreSignedObjUrls(List<String> objectNames, int expireSecond) throws Exception;
}