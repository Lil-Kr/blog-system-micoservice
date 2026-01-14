package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
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
