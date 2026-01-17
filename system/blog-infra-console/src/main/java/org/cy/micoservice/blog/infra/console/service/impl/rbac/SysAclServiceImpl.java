package org.cy.micoservice.blog.infra.console.service.impl.rbac;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.*;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.acl.AclReq;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.acl.SysAclResp;
import org.cy.micoservice.blog.infra.console.dao.rbac.*;
import org.cy.micoservice.blog.infra.console.service.RbacCacheService;
import org.cy.micoservice.blog.infra.console.service.SysAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;
import static org.cy.micoservice.blog.infra.console.constant.CommonConstants.ACLM_PREV_INFO;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Service
@Slf4j
public class SysAclServiceImpl extends ServiceImpl<SysAclMapper, SysAcl> implements SysAclService {

  @Autowired
  private SysAclMapper aclMapper;

  @Autowired
  private SysRoleAdminMapper roleUserMapper;

  @Autowired
  private SysRoleAclMapper roleAclMapper;

  @Autowired
  private SysRoleMapper roleMapper;

  @Autowired
  private SysAdminMapper adminMapper;

  @Autowired
  private RbacCacheService rbacCacheService;

  /**
   * 添加权限点
   *
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public ApiResp<String> add(AclReq req) {
    QueryWrapper<SysAcl> query = new QueryWrapper<>();
    query.eq("name", req.getName());
    query.eq("acl_module_id", req.getAclModuleId());
    if (aclMapper.selectCount(query) >= 1) {
      return ApiResp.warning(DATA_INFO_REPEAT);
    }

    /**
     * 每个权限模块下只能配置一个菜单类型的权限点
     */
    QueryWrapper<SysAcl> query2 = new QueryWrapper<>();
    // 菜单类型需要检查重复, 每个权限模块下只能有一个菜单类型权限点
    if (req.getType() == 1) {
      query2.eq("type", req.getType());
      query2.eq("acl_module_id", req.getAclModuleId());
      SysAcl acl = aclMapper.selectOne(query2);
      if (Objects.nonNull(acl)) {
        return ApiResp.warning("权限模块只能有一个菜单类型的权限");
      }
    }

    Long surrogateId = IdWorker.getSnowFlakeId(); // surrogateId
    LocalDateTime currentTime = DateUtil.localDateTimeNow();// 当前时间
    SysAcl build = SysAcl.builder()
      .aclId(surrogateId)
      .number(ACLM_PREV_INFO + surrogateId)
      .name(req.getName())
      .aclModuleId(req.getAclModuleId())
      .url(req.getUrl())
      .menuName(StringUtils.isBlank(req.getMenuName()) ? "-" : req.getMenuName())
      .menuUrl(StringUtils.isBlank(req.getMenuUrl()) ? "-" : req.getMenuUrl())
      .btnSign(StringUtils.isBlank(req.getBtnSign()) ? "-" : req.getBtnSign())
      .type(req.getType())
      .status(req.getStatus())
      .seq(req.getSeq())
      .remark(req.getRemark())
      .createId(req.getAdminId())
      .updateId(req.getAdminId())
      .operateIp("127.0.0.1")
      .createTime(currentTime)
      .updateTime(currentTime)
      .build();

    int insert = aclMapper.insert(build);
    if (insert < 1) {
      return ApiResp.failure(ADD_ERROR);
    }

    // 更新缓存
    rbacCacheService.invalidAllUserAclCache();
    return ApiResp.success("添加权限点成功");
  }

  /**
   * 更新权限点
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public ApiResp<String> edit(AclReq req) {
    QueryWrapper<SysAcl> query = new QueryWrapper<>();
    query.eq("acl_id", req.getAclId());
    SysAcl before = aclMapper.selectOne(query);
    if (Objects.isNull(before)) {
      return ApiResp.warning(INFO_NOT_EXIST);
    }

    /**
     * 检查当需要修改的权限点类型
     */
    if (req.getType() == 1) {
      QueryWrapper<SysAcl> query1 = new QueryWrapper<>();
      query1.eq("acl_module_id", req.getAclModuleId());
      query1.eq("type", 1);
      SysAcl acl = aclMapper.selectOne(query1);
      // 如果之前不是菜单权限, 并且已经存在菜单权限, 是不合法的业务
      if (Objects.nonNull(acl) && before.getType() != 1) {
        return ApiResp.warning("权限模块只能有一个菜单类型的权限");
      }
    }

    // 当前时间
    LocalDateTime currentTime = DateUtil.localDateTimeNow();
    SysAcl build = SysAcl.builder()
      .name(req.getName())
      .aclModuleId(req.getAclModuleId())
      .url(req.getUrl())
      .type(req.getType())
      .status(req.getStatus())
      .seq(req.getSeq())
      .menuName(StringUtils.isBlank(req.getMenuName()) ? "-" : req.getMenuName())
      .menuUrl(StringUtils.isBlank(req.getMenuUrl()) ? "-" : req.getMenuUrl())
      .btnSign(StringUtils.isBlank(req.getBtnSign()) ? "-" : req.getBtnSign())
      .remark(req.getRemark())
      .updateId(req.getAdminId())
      .operateIp("127.0.0.1")
      .updateTime(currentTime)
      .build();

    UpdateWrapper<SysAcl> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("acl_id", req.getAclId());
    int update = aclMapper.update(build, updateWrapper);
    if (update < 1) {
      return ApiResp.warning(UPDATE_ERROR);
    }
    // 更新缓存
    rbacCacheService.invalidAllUserAclCache();
    return ApiResp.success(SUCCESS);
  }

  /**
   * 分页查询权限点列表
   *
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public PageResult<SysAclResp> pageList(AclPageReq req) {
    List<SysAclResp> list = aclMapper.pageAclList(req);
    Integer count = aclMapper.countPageAclList(req);
    if (CollectionUtils.isEmpty(list)) {
      return new PageResult<>(new ArrayList<>(0), 0);
    }
    return new PageResult<>(list, count);
  }

  /**
   * 获取权限点分配的用户角色
   *
   * @param req
   * @return
   * @throws Exception
   */
  @Override
  public ApiResp<ConcurrentHashMap<String, Object>> acls(AclReq req) {
    ConcurrentHashMap<String, Object> resMap = new ConcurrentHashMap<>();

    // 查询权限对应的角色id
    QueryWrapper<SysRoleAcl> query1 = new QueryWrapper<>();
    query1.select("role_id")
      .eq("acl_id", req.getAclId());
    List<SysRoleAcl> roleIdList = roleAclMapper.selectList(query1);
    Set<Long> roleIdSet = roleIdList.stream().map(SysRoleAcl::getAclId).collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(roleIdSet)) {
      resMap.put("users", Lists.newArrayList());
    }

    // 根据角色id查询用户id
    QueryWrapper<SysRoleAdmin> query2 = new QueryWrapper<>();
    query2.select("user_id")
      .in("role_id", Lists.newArrayList(roleIdSet));
    List<SysRoleAdmin> userIdList = roleUserMapper.selectList(query2);
    Set<Long> userIdSet = userIdList.stream().map(SysRoleAdmin::getUserId).collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(userIdSet)) {
      resMap.put("roles", Lists.newArrayList());
    }

    // 根据角色id查询具体的角色信息
    QueryWrapper<SysRole> query3 = new QueryWrapper<>();
    query3.in("surrogate_id", Lists.newArrayList(roleIdSet));
    List<SysRole> roleList = roleMapper.selectList(query3);

    // 根据用户id查询用户详细信息
    QueryWrapper<SysAdmin> query4 = new QueryWrapper<>();
    query4.in("surrogate_id", Lists.newArrayList(userIdSet));
    List<SysAdmin> userList = adminMapper.selectList(query4);

    resMap.put("users", userList);
    resMap.put("roles", roleList);
    return ApiResp.success(resMap);
  }

  /**
   * 删除权限点
   *
   * @param id
   * @return
   */
  @Override
  public ApiResp<String> delete(Long id) {
    QueryWrapper<SysAcl> wrapper = new QueryWrapper<>();
    wrapper.eq("surrogate_id", id);
    int delete = aclMapper.delete(wrapper);
    if (delete < 1) {
      return ApiResp.failure(DEL_ERROR);
    }
    // 更新缓存
    rbacCacheService.invalidAllUserAclCache();
    return ApiResp.success();
  }

  /**
   * 查询权限模块下的权限点数量
   *
   * @param aclModuleId
   * @return
   */
  @Override
  public Long getAclCountByAclModuleId(Long aclModuleId) {
    QueryWrapper<SysAcl> wrapper = new QueryWrapper<>();
    wrapper.eq("acl_module_id", aclModuleId);
    Long count = aclMapper.selectCount(wrapper);
    return count;
  }
}
