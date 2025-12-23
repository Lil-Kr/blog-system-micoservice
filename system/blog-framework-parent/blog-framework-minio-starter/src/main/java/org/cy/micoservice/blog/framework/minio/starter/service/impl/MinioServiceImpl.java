package org.cy.micoservice.blog.framework.minio.starter.service.impl;

import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.framework.minio.starter.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: MinIO服务实现类
 */
@Service
public class MinioServiceImpl implements MinioService {

  @Autowired
  private MinioClient minioClient;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Value("${minio.bucket.name:}")
  private String bucketName;

  private static final String MINIO = "minio";

  private static final String OBJECT_NAME_PREFIX = MINIO + ":%s";

  /**
   * 上传文件
   *
   * @param file       要上传的文件
   * @param objectName 对象名称
   * @throws Exception
   */
  @Override
  public void uploadFile(MultipartFile file, String objectName) throws Exception {
    try (InputStream inputStream = file.getInputStream()) {
      PutObjectArgs args = PutObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .stream(inputStream, file.getSize(), -1)
        .contentType(file.getContentType())
        .build();
      minioClient.putObject(args);
    }
  }

  /**
   * 下载文件
   *
   * @param objectName 对象名称
   * @return 文件输入流
   * @throws Exception
   */
  @Override
  public InputStream downloadFile(String objectName) throws Exception {
    GetObjectArgs args = GetObjectArgs.builder()
      .bucket(bucketName)
      .object(objectName)
      .build();
    return minioClient.getObject(args);
  }

  /**
   * 删除文件
   *
   * @param objectName 对象名称
   * @throws Exception
   */
  @Override
  public void deleteFile(String objectName) throws Exception {
    RemoveObjectArgs args = RemoveObjectArgs.builder()
      .bucket(bucketName)
      .object(objectName)
      .build();
    minioClient.removeObject(args);
  }

  /**
   * 检查文件是否存在
   *
   * @param objectName 对象名称
   * @return 文件是否存在
   * @throws Exception
   */
  @Override
  public boolean isFileExists(String objectName) throws Exception {
    StatObjectArgs args = StatObjectArgs.builder()
      .bucket(bucketName)
      .object(objectName)
      .build();
    minioClient.statObject(args);
    return true;
  }

  /**
   * 获取私有桶中文件的预签名 URL
   * @param objectName   要访问的文件对象名称
   * @param expireSecond 预签名 URL 的有效时长（秒）
   * @return 预签名 URL
   */
  @Override
  public String getPreSignedObjUrl(String objectName, int expireSecond) throws Exception {
    if (expireSecond < 10) {
      throw new IllegalArgumentException("expireSecond must grater than 10");
    }
    String cacheStr = redisTemplate.opsForValue().get(String.format(OBJECT_NAME_PREFIX, objectName));
    if (StringUtils.isNotEmpty(cacheStr)) {
      return cacheStr;
    }
    GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
      .method(Method.GET)
      .bucket(bucketName)
      .object(objectName)
      .expiry(expireSecond)
      .build();
    String objectUrl = minioClient.getPresignedObjectUrl(args);
    if (StringUtils.isEmpty(objectUrl)) {
      return null;
    }
    redisTemplate.opsForValue().set(String.format(OBJECT_NAME_PREFIX, objectName), objectUrl, expireSecond - 5);
    return objectUrl;
  }

  @Override
  public Map<String, String> batchGetPreSignedObjUrls(List<String> objectNames, int expireSecond) throws Exception {
    if (expireSecond < 10) {
      throw new IllegalArgumentException("expireSecond must grater than 10");
    }
    List<String> objectNameList = objectNames.stream().map(objectName -> String.format(OBJECT_NAME_PREFIX, objectName)).toList();
    List<String> cacheObjUrls = Optional.ofNullable(redisTemplate.opsForValue().multiGet(objectNameList)).orElse(new ArrayList<>());
    int i = 0;
    List<String> missCacheObjNames = new ArrayList<>();
    Map<String, String> resultMap = new HashMap<>();
    for (String cacheObjUrl : cacheObjUrls) {
      String objectName = objectNames.get(i);
      if (StringUtils.isEmpty(cacheObjUrl)) {
        missCacheObjNames.add(objectName);
      } else {
        resultMap.put(objectName, cacheObjUrl);
      }
      i++;
    }
    //未命中缓存的objectName做一轮查询
    if (CollectionUtils.isNotEmpty(missCacheObjNames)) {
      for (String objectName : missCacheObjNames) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
          .method(Method.GET)
          .bucket(bucketName)
          .object(objectName)
          .expiry(expireSecond)
          .build();
        String objectUrl = minioClient.getPresignedObjectUrl(args);
        redisTemplate.opsForValue().set(String.format(OBJECT_NAME_PREFIX,objectName),objectUrl,expireSecond - 5, TimeUnit.SECONDS);
        resultMap.put(objectName, objectUrl);
      }
    }
    return resultMap;
  }
}
