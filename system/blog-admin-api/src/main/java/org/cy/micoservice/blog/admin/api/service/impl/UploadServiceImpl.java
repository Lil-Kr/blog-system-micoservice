package org.cy.micoservice.blog.admin.api.service.impl;

import org.cy.micoservice.blog.admin.api.service.UploadService;
import org.cy.micoservice.blog.entity.admin.model.req.image.ImageUploadReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AvatarUploadReq;

import java.io.IOException;

/**
 * @Author: Lil-K
 * @Date: 2025/4/20
 * @Description:
 */
public class UploadServiceImpl implements UploadService {

  @Override
  public boolean uploadImage(String fullPath, ImageUploadReq req) throws IOException {
    return false;
  }

  @Override
  public boolean uploadAvatar(String fullPath, AvatarUploadReq req) throws IOException {
    return false;
  }
}
