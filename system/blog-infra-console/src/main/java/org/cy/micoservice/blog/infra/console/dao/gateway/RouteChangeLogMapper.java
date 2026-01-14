package org.cy.micoservice.blog.infra.console.dao.gateway;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Repository
public interface RouteChangeLogMapper extends BaseMapper<RouteChangeLog> {

  List<RouteChangeLog> findGtVersion(Long version);

}