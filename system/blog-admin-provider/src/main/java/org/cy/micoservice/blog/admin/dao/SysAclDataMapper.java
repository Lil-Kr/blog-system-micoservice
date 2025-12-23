package org.cy.micoservice.blog.admin.dao;

import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAclData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SysAclDataMapper继承基类
 */
@Repository
public interface SysAclDataMapper {

	List<SysAclData> selectAclDataListByAclIds(@Param("aclList") List<Long> aclList);
}