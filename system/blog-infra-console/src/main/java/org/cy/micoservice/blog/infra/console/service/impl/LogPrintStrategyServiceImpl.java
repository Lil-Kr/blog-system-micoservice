package org.cy.micoservice.blog.infra.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.enums.biz.ValidStatusEnum;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyPageReq;
import org.cy.micoservice.blog.infra.console.dao.LogPrintStrategyMapper;
import org.cy.micoservice.blog.infra.console.service.LogPrintStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 网关打印策略
 */
@Service
@Slf4j
public class LogPrintStrategyServiceImpl implements LogPrintStrategyService {

  @Autowired
  private LogPrintStrategyMapper logPrintStrategyMapper;

  @Override
  public PageResult<LogPrintStrategy> pagePrintStrategyList(LogPrintStrategyPageReq req) {
    req.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    List<LogPrintStrategy> result = logPrintStrategyMapper.pagePrintStrategyList(req);
    if (CollectionUtils.isEmpty(result)) {
      return new PageResult<>(result, 0);
    }
    Integer count = logPrintStrategyMapper.pagePrintStrategyCount(req);
    return new PageResult<>(result, count);
  }

  @Override
  public ApiResp<String> add(LogPrintStrategyAddReq req) {
    QueryWrapper<LogPrintStrategy> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("strategy_name", req.getStrategyName());
    LogPrintStrategy before = logPrintStrategyMapper.selectOne(queryWrapper);
    if (Objects.nonNull(before)) return ApiResp.failure(ApiReturnCodeEnum.INFO_EXIST);

    LogPrintStrategy convert = BeanCopyUtils.convert(req, LogPrintStrategy.class);
    convert.setId(IdWorker.getSnowFlakeId());
    convert.setStatus(ValidStatusEnum.ACTIVE.getCode());
    convert.setDeleted(DeleteStatusEnum.ACTIVE.getCode());
    // todo: 这里修改为当前操作人
    convert.setCreatorId(convert.getId());
    convert.setOperatorId(convert.getId());

    LocalDateTime now = DateUtil.localDateTimeNow();
    convert.setCreateTime(now);
    convert.setUpdateTime(now);

    int insert = logPrintStrategyMapper.insert(convert);
    return insert > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.ADD_ERROR);
  }

  @Override
  public ApiResp<String> edit(LogPrintStrategyEditReq req) {
    QueryWrapper<LogPrintStrategy> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", req.getId());
    LogPrintStrategy before = logPrintStrategyMapper.selectOne(queryWrapper);
    if (Objects.isNull(before)) return ApiResp.failure(ApiReturnCodeEnum.INFO_NOT_EXIST);

    LogPrintStrategy convert = BeanCopyUtils.convert(req, LogPrintStrategy.class);
    convert.setId(before.getId());
    // todo: 这里修改为当前操作人
    convert.setOperatorId(convert.getId());
    convert.setUpdateTime(DateUtil.localDateTimeNow());
    int update = logPrintStrategyMapper.updateById(convert);
    return update > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.UPDATE_ERROR);
  }

  @Override
  public ApiResp<String> delete(Long id) {
    LogPrintStrategy entity = new LogPrintStrategy();
    entity.setId(id);
    entity.setDeleted(DeleteStatusEnum.DELETED.getCode());
    int delete = logPrintStrategyMapper.updateById(entity);
    return delete > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.DEL_ERROR);
  }
}
