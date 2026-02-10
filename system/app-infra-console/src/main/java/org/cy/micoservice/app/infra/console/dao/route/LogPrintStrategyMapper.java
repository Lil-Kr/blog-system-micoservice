package org.cy.micoservice.app.infra.console.dao.route;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.app.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.app.entity.gateway.model.req.LogPrintStrategyPageReq;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 日志打印策略 mapper
 */
@Repository
public interface LogPrintStrategyMapper extends BaseMapper<LogPrintStrategy> {

  List<LogPrintStrategy> pagePrintStrategyList(@Param("param") LogPrintStrategyPageReq req);

  Integer pagePrintStrategyCount(@Param("param") LogPrintStrategyPageReq req);
}