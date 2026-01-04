package org.cy.micoservice.blog.infra.console.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryPageReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryReq;
import org.cy.micoservice.blog.gateway.facade.dto.ChangeBodyDTO;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterChangeEventEnum;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterDeletedEnum;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterStatusEnum;
import org.cy.micoservice.blog.infra.console.config.InfraCacheKeyBuilder;
import org.cy.micoservice.blog.infra.console.constant.InfraConstants;
import org.cy.micoservice.blog.infra.console.dao.RouteConfigMapper;
import org.cy.micoservice.blog.infra.console.service.NacosService;
import org.cy.micoservice.blog.infra.console.service.RouteConfigChangeLogService;
import org.cy.micoservice.blog.infra.console.service.RouteConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Slf4j
@Service
public class RouteConfigServiceImpl implements RouteConfigService {

  @Autowired
  private RouteConfigMapper routeConfigMapper;
  @Autowired
  private RouteConfigChangeLogService routeConfigChangeLogService;
  @Autowired
  private NacosService nacosService;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private InfraCacheKeyBuilder infraCacheKeyBuilder;
  // cache key
  private final String LOCK_KEY = infraCacheKeyBuilder.changeRouteConfigKey(InfraConstants.CHANGE_ROUTE_CONFIG_KEY);

  @Override
  public PageResult<RouteConfig> pageRouteConfigList(RouteConfigQueryPageReq req) {
    List<RouteConfig> pageList = routeConfigMapper.pageRouteConfigList(req);
    if (CollectionUtils.isEmpty(pageList)) {
      return PageResult.emptyPage();
    }
    Integer count = routeConfigMapper.pageRouteConfigListCount(req);
    return new PageResult<>(pageList, count);
  }

  @Override
  public ApiResp<List<RouteConfig>> routeConfigList(RouteConfigQueryReq req) {
    List<RouteConfig> configs = routeConfigMapper.routeConfigList(req);
    return ApiResp.success(configs);
  }

  /**
   * insert --> config statue is invalid
   * @param req
   * @return
   * @throws NacosException
   */
  @Override
  public ApiResp<Long> create(RouteConfigAddReq req) throws Exception {
    /**
     * 加入分布式锁, 3秒释放
     */
    Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", 3, TimeUnit.SECONDS);
    AssertUtil.isTrue(lockStatus, ApiReturnCodeEnum.SYSTEM_ERROR);

//    QueryWrapper<RouteConfig> wrapper = new QueryWrapper<>();
//    wrapper.eq("`method`", req.getMethod());
//    wrapper.eq("`path`", req.getPath());
//    RouteConfig config = routeConfigMapper.selectOne(wrapper);
//    if (Objects.nonNull(config)) {
//      return ApiResp.failure(ReturnCodeEnum.INFO_EXIST);
//    }

    RouteConfig routeConfig = new RouteConfig();
    BeanUtils.copyProperties(req, routeConfig);

    routeConfig.setId(IdWorker.getSnowFlakeId());
    routeConfig.setStatus(GatewayRouterStatusEnum.INVALID.getCode());
    routeConfig.setCreateBy("admin");
    routeConfig.setUpdateBy("admin");

    LocalDateTime nowDate = DateUtil.localDateTimeNow();
    routeConfig.setCreateTime(nowDate);
    routeConfig.setUpdateTime(nowDate);
    routeConfig.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    int insert = routeConfigMapper.insert(routeConfig);

    // insert route change log
    RouteChangeLog routeChangeLog = new RouteChangeLog();
    routeChangeLog.setId(IdWorker.getSnowFlakeId());
    routeChangeLog.setConfigId(routeConfig.getId());
    routeChangeLog.setChangeEvent(GatewayRouterChangeEventEnum.INSERT.getCode());

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(new RouteConfig(), routeConfig);
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));
    routeChangeLog.setCreateBy("admin");
    routeChangeLog.setCreateTime(nowDate);
    routeChangeLog.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    Integer insertLog = routeConfigChangeLogService.create(routeChangeLog);

    // release lock
    redisTemplate.delete(LOCK_KEY);
    return insert > 0 && insertLog > 0 ? ApiResp.success(routeConfig.getId()) : ApiResp.failure(ApiReturnCodeEnum.ADD_ERROR);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResp<String> edit(RouteConfigEditReq req) throws NacosException {
    /**
     * 加入分布式锁, 3秒释放
     */
    Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", 3, TimeUnit.SECONDS);
    AssertUtil.isTrue(lockStatus, ApiReturnCodeEnum.SYSTEM_ERROR);

    RouteConfig before = routeConfigMapper.selectById(req.getId());
    if (Objects.isNull(before)) {
      return ApiResp.failure(ApiReturnCodeEnum.INFO_NOT_EXIST);
    }

    RouteConfig after = new RouteConfig();
    BeanUtils.copyProperties(before, after);
    BeanUtils.copyProperties(req, after);
    after.setId(before.getId());
    after.setUpdateBy("admin");
    after.setUpdateTime(DateUtil.localDateTimeNow());
    int update = routeConfigMapper.updateById(after);

    // insert route change log
    RouteChangeLog routeChangeLog = new RouteChangeLog();
    BeanUtils.copyProperties(after, routeChangeLog);

    routeChangeLog.setId(IdWorker.getSnowFlakeId());
    routeChangeLog.setConfigId(before.getId());
    routeChangeLog.setChangeEvent(GatewayRouterChangeEventEnum.UPDATE.getCode());

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(before, after);
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));
    routeChangeLog.setCreateBy("admin");
    routeChangeLog.setCreateTime(DateUtil.localDateTimeNow());
    routeChangeLog.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    // 触发 nacos 更新版本
    Long version = nacosService.incrVersion();
    routeChangeLog.setVersion(version);
    int insertLog = routeConfigChangeLogService.create(routeChangeLog);

    // release lock
    redisTemplate.delete(LOCK_KEY);
    return update > 0 && insertLog > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.UPDATE_ERROR);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResp<String> delete(Long configId) throws NacosException {
    /**
     * 加入分布式锁, 3秒释放
     */
    Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", 3, TimeUnit.SECONDS);
    AssertUtil.isTrue(lockStatus, ApiReturnCodeEnum.SYSTEM_ERROR);

    RouteConfig before = routeConfigMapper.selectById(configId);
    if (Objects.isNull(before)) {
      return ApiResp.failure(ApiReturnCodeEnum.DEL_ERROR);
    }
    int del = routeConfigMapper.deleteById(before.getId());

    // insert route change log
    RouteChangeLog routeChangeLog = new RouteChangeLog();
    routeChangeLog.setId(IdWorker.getSnowFlakeId());
    routeChangeLog.setConfigId(before.getId());
    // 触发 nacos 更新版本
    Long version = nacosService.incrVersion();
    routeChangeLog.setVersion(version);
    routeChangeLog.setChangeEvent(GatewayRouterChangeEventEnum.DELETED.getCode());

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(before, new RouteConfig());
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));

    routeChangeLog.setCreateBy("admin");
    routeChangeLog.setCreateTime(DateUtil.localDateTimeNow());
    routeChangeLog.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    int insertLog = routeConfigChangeLogService.create(routeChangeLog);

    // release lock
    redisTemplate.delete(LOCK_KEY);
    return del > 0 && insertLog > 0 ? ApiResp.success() : ApiResp.failure(ApiReturnCodeEnum.DEL_ERROR);
  }

  @Override
  public ApiResp<List<RouteChangeLog>> getConfigLog(Long configId) {
    List<RouteChangeLog> logList = routeConfigChangeLogService.selectById(configId);
    return ApiResp.success(logList);
  }
}
