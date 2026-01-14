package org.cy.micoservice.blog.admin.facade.interfaces;

import org.cy.micoservice.blog.entity.admin.model.req.image.ImageUploadReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AvatarUploadReq;

import java.io.IOException;

/**
 * @Author: Lil-K
 * @Date: 2025/4/20
 * @Description: upload file service
 */
public interface UploadFacade {

  boolean uploadImage(String fullPath, ImageUploadReq req) throws IOException;

  boolean uploadAvatar(String fullPath, AvatarUploadReq req) throws IOException;
}
