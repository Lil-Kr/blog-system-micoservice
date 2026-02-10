package org.cy.micoservice.app.infra.console.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysDict;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.dict.DictListPageReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.dic.SysDictResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-29
 */
@Repository
public interface SysDictMapper extends BaseMapper<SysDict> {

  SysDictResp getDict(@Param("surrogateId") Long surrogateId);

  List<SysDictResp> pageDictList(@Param("param") DictListPageReq req);

  Integer countPageDict(@Param("param") DictListPageReq req);

  List<SysDict> selectDictList();

}
