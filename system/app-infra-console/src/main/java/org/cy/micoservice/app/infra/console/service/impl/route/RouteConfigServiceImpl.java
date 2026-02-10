package org.cy.micoservice.app.infra.console.service.impl.route;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.app.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.app.common.utils.AssertUtil;
import org.cy.micoservice.app.common.utils.BeanCopyUtils;
import org.cy.micoservice.app.common.utils.DateUtil;
import org.cy.micoservice.app.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.app.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.app.entity.gateway.model.req.*;
import org.cy.micoservice.app.infra.console.constant.InfraConstants;
import org.cy.micoservice.app.infra.console.dao.route.RouteConfigMapper;
import org.cy.micoservice.app.infra.console.service.NacosService;
import org.cy.micoservice.app.infra.console.service.RouteConfigChangeLogService;
import org.cy.micoservice.app.infra.console.service.RouteConfigService;
import org.cy.micoservice.app.framework.id.starter.service.IdService;
import org.cy.micoservice.app.gateway.facade.dto.ChangeBodyDTO;
import org.cy.micoservice.app.gateway.facade.enums.GatewayRouterChangeEventEnum;
import org.cy.micoservice.app.gateway.facade.enums.GatewayRouterDeletedEnum;
import org.cy.micoservice.app.gateway.facade.enums.GatewayRouterSchemaEnum;
import org.cy.micoservice.app.gateway.facade.enums.GatewayRouterStatusEnum;
import org.cy.micoservice.app.infra.console.config.InfraCacheKeyBuilder;
import org.springframework.beans.factory.InitializingBean;
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
public class RouteConfigServiceImpl implements RouteConfigService, InitializingBean {

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
  @Autowired
  private IdService idService;
  // cache key
  private String LOCK_KEY;

  @Override
  public PageResult<RouteConfig> pageRouteConfigList(RouteConfigQueryPageReq req) {
    List<RouteConfig> pageList = routeConfigMapper.pageRouteConfigList(req);
    Integer count = routeConfigMapper.countPageRouteConfigList(req);
    if (CollectionUtils.isEmpty(pageList)) {
      return PageResult.emptyPage();
    }
    return new PageResult<>(pageList, count);
  }

  @Override
  public ApiResp<List<RouteConfig>> routeConfigList(RouteConfigQueryReq req) {
    List<RouteConfig> configs = routeConfigMapper.routeConfigList(req);
    return ApiResp.success(configs);
  }

  @Override
  public ApiResp<List<RouteConfig>> routeConfigInternalList(RouteConfigQueryReq req) {
    return this.routeConfigList(req);
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

    if (GatewayRouterSchemaEnum.DUBBO.getCode().equals(req.getSchema())) {
      req.setUri(GatewayInfraConsoleSdkConstants.DUBBO_URL_PREFIX + req.getProviderInterface() + "#" + req.getProviderInterfaceMethod());
    }

    RouteConfig routeConfig = BeanCopyUtils.convert(req, RouteConfig.class);
    routeConfig.setId(idService.getId());
    routeConfig.setStatus(GatewayRouterStatusEnum.INVALID.getCode());
    routeConfig.setCreateId(req.getAdminId());
    routeConfig.setUpdateId(req.getAdminId());

    LocalDateTime nowDate = DateUtil.localDateTimeNow();
    routeConfig.setCreateTime(nowDate);
    routeConfig.setUpdateTime(nowDate);
    routeConfig.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    int insert = routeConfigMapper.insert(routeConfig);

    // insert route change log
    RouteChangeLog routeChangeLog = RouteChangeLog.builder()
      .id(idService.getId())
      .configId(routeConfig.getId())
      .changeEvent(GatewayRouterChangeEventEnum.INSERT.getCode())
      .version(0L)
      .build();

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(RouteConfig.builder().build(), routeConfig);
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));
    routeChangeLog.setCreateId(req.getAdminId());
    routeChangeLog.setUpdateId(req.getAdminId());
    routeChangeLog.setCreateTime(nowDate);
    routeChangeLog.setUpdateTime(nowDate);
    routeChangeLog.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    Integer insertLog = routeConfigChangeLogService.create(routeChangeLog);

    // release lock
    redisTemplate.delete(LOCK_KEY);
    return insert > 0 && insertLog > 0 ? ApiResp.success(routeConfig.getId()) : ApiResp.failure(ApiReturnCodeEnum.ADD_ERROR);
  }

  /**
   * 内部调用
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public ApiResp<Long> createInternal(RouteConfigAddReq req) throws Exception {
    return this.create(req);
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

    RouteConfig after = BeanCopyUtils.convert(req, RouteConfig.class);
    after.setId(before.getId());
    after.setUpdateId(req.getAdminId());
    after.setUpdateTime(DateUtil.localDateTimeNow());
    int update = routeConfigMapper.updateById(after);

    // insert route change log
    RouteChangeLog routeChangeLog = BeanCopyUtils.convert(after, RouteChangeLog.class);

    routeChangeLog.setId(idService.getId());
    routeChangeLog.setConfigId(before.getId());
    routeChangeLog.setChangeEvent(GatewayRouterChangeEventEnum.UPDATE.getCode());

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(before, after);
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));
    routeChangeLog.setCreateId(req.getAdminId());
    routeChangeLog.setUpdateId(req.getAdminId());
    LocalDateTime now = DateUtil.localDateTimeNow();
    routeChangeLog.setCreateTime(now);
    routeChangeLog.setUpdateTime(now);
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
  public ApiResp<String> delete(RouteConfigDelReq req) throws NacosException {
    /**
     * 加入分布式锁, 3秒释放
     */
    Boolean lockStatus = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", 3, TimeUnit.SECONDS);
    AssertUtil.isTrue(lockStatus, ApiReturnCodeEnum.SYSTEM_ERROR);

    RouteConfig before = routeConfigMapper.selectById(req.getId());
    if (Objects.isNull(before)) {
      return ApiResp.failure(ApiReturnCodeEnum.DEL_ERROR);
    }
    before.setDeleted(GatewayRouterDeletedEnum.DELETED.getCode());
    int del = routeConfigMapper.updateById(before);

    // insert route change log
    RouteChangeLog routeChangeLog = RouteChangeLog.builder()
      .id(idService.getId())
      .configId(before.getId())
      .build();
    // 触发 nacos 更新版本
    Long version = nacosService.incrVersion();
    routeChangeLog.setVersion(version);
    routeChangeLog.setChangeEvent(GatewayRouterChangeEventEnum.DELETED.getCode());

    ChangeBodyDTO changeBodyDTO = new ChangeBodyDTO(before, RouteConfig.builder().build());
    routeChangeLog.setChangeBody(JSONObject.toJSONString(changeBodyDTO));

    routeChangeLog.setCreateId(req.getAdminId());
    routeChangeLog.setUpdateId(req.getAdminId());
    routeChangeLog.setDeleted(GatewayRouterDeletedEnum.ACTIVE.getCode());
    LocalDateTime now = DateUtil.localDateTimeNow();
    routeChangeLog.setCreateTime(now);
    routeChangeLog.setUpdateTime(now);
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

  @Override
  public ApiResp<List<RouteConfig>> getAppNameList() {
    List<RouteConfig> appNameList = routeConfigMapper.getAppNameList();
    return ApiResp.success(appNameList);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.LOCK_KEY = infraCacheKeyBuilder.changeRouteConfigKey(InfraConstants.CHANGE_ROUTE_CONFIG_KEY);
  }
}
