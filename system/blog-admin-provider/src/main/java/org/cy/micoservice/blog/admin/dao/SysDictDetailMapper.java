package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictDetailResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/9
 * @Description:
 */
@Repository
public interface SysDictDetailMapper extends BaseMapper<SysDictDetail> {

  List<SysDictDetailResp> getDictDetailListByParentId(@Param("dictSurrogateId") Long dictSurrogateId);

  List<SysDictDetailResp> pageDictDetailListById(@Param("param") DictDetailPageListReq req);

  Integer countPageDictDetail(@Param("param") DictDetailPageListReq req);

  List<SysDictDetailResp> dictDetailList();
}
