package org.cy.micoservice.app.infra.console.service.impl.route;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.app.common.enums.biz.ValidStatusEnum;
import org.cy.micoservice.app.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.app.common.utils.BeanCopyUtils;
import org.cy.micoservice.app.common.utils.DateUtil;
import org.cy.micoservice.app.common.utils.IdWorker;
import org.cy.micoservice.app.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.app.entity.gateway.model.req.LogPrintStrategyAddReq;
import org.cy.micoservice.app.entity.gateway.model.req.LogPrintStrategyEditReq;
import org.cy.micoservice.app.entity.gateway.model.req.LogPrintStrategyPageReq;
import org.cy.micoservice.app.entity.gateway.model.req.RouteConfigLogDelReq;
import org.cy.micoservice.app.infra.console.dao.route.LogPrintStrategyMapper;
import org.cy.micoservice.app.infra.console.service.LogPrintStrategyService;
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
@Slf4j
@Service
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
    convert.setCreateId(req.getAdminId());
    convert.setUpdateId(req.getAdminId());

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
    convert.setUpdateId(convert.getId());
    convert.setUpdateTime(DateUtil.localDateTimeNow());
    int update = logPrintStrategyMapper.updateById(convert);
    return update > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.UPDATE_ERROR);
  }

  @Override
  public ApiResp<String> delete(RouteConfigLogDelReq req) {
    LogPrintStrategy entity = LogPrintStrategy.builder()
      .id(req.getId())
      .updateId(req.getAdminId())
      .deleted(DeleteStatusEnum.DELETED.getCode())
      .build();
    int delete = logPrintStrategyMapper.updateById(entity);
    return delete > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.DEL_ERROR);
  }
}
