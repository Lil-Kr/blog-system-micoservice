package org.cy.micoservice.blog.entity.admin.model.dto.user;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.EncryptUtils;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import org.cy.micoservice.blog.entity.admin.model.req.user.UserRegisterReq;
import org.cy.micoservice.blog.entity.admin.model.req.user.UserSaveReq;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
public class UserDTO {

  private static final String ACCOUNT_RANDOM = "blog-";
  private static final String NUMBER_PREFIX = "R";
  private static final String DEFAULT_PWD = "123456";

  /**
   * request param convert to save admin object
   * @param baseReq
   * @return
   */
  public static SysUser convertSaveAdminReq(UserRegisterReq baseReq) {
    SysUser adminUser = SysUser.builder().build();
    BeanUtils.copyProperties(baseReq, adminUser);

    adminUser.setNumber(ACCOUNT_RANDOM + IdWorker.generateRandomStr(10));

    // 默认密码
    adminUser.setPassword(EncryptUtils.md5(DEFAULT_PWD));
    // token
    adminUser.setToken(IdWorker.generateUUID());

    Long id = IdWorker.getSnowFlakeId();
    adminUser.setSurrogateId(id);
    adminUser.setCreatorId(id);
    adminUser.setOperator(id);

    adminUser.setStatus(0);
    adminUser.setDeleted(0);
    adminUser.setOperateIp("0.0.0.0");

    Date nowDateTime = DateUtil.localDateTimeNow();
    adminUser.setCreateTime(nowDateTime);
    adminUser.setUpdateTime(nowDateTime);
    return adminUser;
  }

  public static SysUser convertAddUserReq(UserSaveReq req) {
    SysUser build = SysUser.builder().build();
    BeanUtils.copyProperties(req, build);

    if (StringUtils.isBlank(build.getNumber()))
      build.setNumber(ACCOUNT_RANDOM + IdWorker.generateRandomStr(10));

    if (StringUtils.isBlank(build.getAccount()))
      build.setAccount(ACCOUNT_RANDOM + IdWorker.generateUUID());

    build.setSurrogateId(IdWorker.getSnowFlakeId());

    build.setToken(IdWorker.generateUUID());

    if (StringUtils.isBlank(build.getPassword()))
      build.setPassword(EncryptUtils.md5(DEFAULT_PWD));

//    build.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
//    build.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    build.setOperateIp("0.0.0.0");
    build.setStatus(0);
    build.setDeleted(0);

    Date nowDateTime = DateUtil.localDateTimeNow();
    build.setCreateTime(nowDateTime);
    build.setUpdateTime(nowDateTime);
    return build;
  }

  public static SysUser convertEditUserReq(SysUser before, UserSaveReq req) {
    BeanUtils.copyProperties(req, before);

//    before.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    before.setUpdateTime(DateUtil.localDateTimeNow());
    return before;
  }
}
