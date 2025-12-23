package org.cy.micoservice.blog.infra.console.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryPageReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryReq;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Repository
public interface RouteConfigMapper extends BaseMapper<RouteConfig> {

  List<RouteConfig> routeConfigAllValidaList(Integer status);

  List<RouteConfig> findInConfigIds(@Param("configIds") Collection<Long> saveConfigIds);

  List<RouteConfig> pageRouteConfigList(@Param("param") RouteConfigQueryPageReq req);

  Integer pageRouteConfigListCount(@Param("param") RouteConfigQueryPageReq req);

  List<RouteConfig> routeConfigList(@Param("param") RouteConfigQueryReq req);
}