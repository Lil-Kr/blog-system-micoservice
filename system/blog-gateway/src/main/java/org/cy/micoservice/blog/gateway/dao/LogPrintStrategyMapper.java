package org.cy.micoservice.blog.gateway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.springframework.stereotype.Repository;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 日志打印策略 mapper
 */
@Repository
public interface LogPrintStrategyMapper extends BaseMapper<LogPrintStrategy> {

}