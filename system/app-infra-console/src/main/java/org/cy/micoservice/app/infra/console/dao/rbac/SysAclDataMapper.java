package org.cy.micoservice.app.infra.console.dao.rbac;

import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAclData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysAclDataMapper继承基类
 */
@Repository
public interface SysAclDataMapper {

	List<SysAclData> selectAclDataListByAclIds(@Param("aclList") List<Long> aclList);
}