package org.cy.micoservice.blog.entity.admin.model.dto.admin;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.EncryptUtils;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminRegisterReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminSaveReq;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
public class AdminDTO {

  private static final String ACCOUNT_RANDOM = "blog-";
  private static final String NUMBER_PREFIX = "R";
  private static final String DEFAULT_PWD = "123456";

  /**
   * request param convert to save admin object
   * @param baseReq
   * @return
   */
  public static SysAdmin convertSaveAdminReq(AdminRegisterReq baseReq) {
    SysAdmin admin = SysAdmin.builder().build();
    BeanUtils.copyProperties(baseReq, admin);

    admin.setNumber(ACCOUNT_RANDOM + IdWorker.generateRandomStr(10));

    // 默认密码
    admin.setPassword(EncryptUtils.md5(DEFAULT_PWD));
    // token
    admin.setToken(IdWorker.generateUUID());

    Long id = IdWorker.getSnowFlakeId();
    admin.setId(id);
    admin.setCreatorId(id);
    admin.setOperator(id);

    admin.setStatus(0);
    admin.setDeleted(0);
    admin.setOperateIp("0.0.0.0");

    Date nowDateTime = DateUtil.dateTimeNow();
    admin.setCreateTime(nowDateTime);
    admin.setUpdateTime(nowDateTime);
    return admin;
  }

  public static SysAdmin convertAddUserReq(AdminSaveReq req) {
    SysAdmin build = SysAdmin.builder().build();
    BeanUtils.copyProperties(req, build);

    if (StringUtils.isBlank(build.getNumber()))
      build.setNumber(ACCOUNT_RANDOM + IdWorker.generateRandomStr(10));

    if (StringUtils.isBlank(build.getAccount()))
      build.setAccount(ACCOUNT_RANDOM + IdWorker.generateUUID());

    build.setId(IdWorker.getSnowFlakeId());

    build.setToken(IdWorker.generateUUID());

    if (StringUtils.isBlank(build.getPassword()))
      build.setPassword(EncryptUtils.md5(DEFAULT_PWD));

//    build.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
//    build.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    build.setOperateIp("0.0.0.0");
    build.setStatus(0);
    build.setDeleted(0);

    Date nowDateTime = DateUtil.dateTimeNow();
    build.setCreateTime(nowDateTime);
    build.setUpdateTime(nowDateTime);
    return build;
  }

  public static SysAdmin convertEditUserReq(SysAdmin before, AdminSaveReq req) {
    BeanUtils.copyProperties(req, before);

//    before.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    before.setUpdateTime(DateUtil.dateTimeNow());
    return before;
  }
}
