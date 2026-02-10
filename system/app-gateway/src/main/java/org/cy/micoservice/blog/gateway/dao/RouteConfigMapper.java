package org.cy.micoservice.blog.gateway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Repository
public interface RouteConfigMapper extends BaseMapper<RouteConfig> {

  List<RouteConfig> routeConfigAllValidaList(Integer status);

  List<RouteConfig> findInConfigIds(@Param("configIds") Set<Long> saveConfigIds);
}