package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.image.ImageInfo;
import org.cy.micoservice.blog.entity.admin.model.req.image.ImageInfoPageListReq;
import org.cy.micoservice.blog.entity.admin.model.resp.image.ImageInfoResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ImageInfoMapper继承基类
 */
@Repository
public interface ImageInfoMapper extends BaseMapper<ImageInfo> {

  List<ImageInfoResp> pageImageInfoList(@Param("param") ImageInfoPageListReq req);

  Integer pageImageInfoListCount(@Param("param") ImageInfoPageListReq req);

  List<ImageInfoResp> imageInfoList(@Param("param") ImageInfoPageListReq req);

  List<ImageInfoResp> pageImageInfoListByCategoryId(Long surrogateId);

  Integer pageImageInfoListByCategoryIdCount(Long surrogateId);
}