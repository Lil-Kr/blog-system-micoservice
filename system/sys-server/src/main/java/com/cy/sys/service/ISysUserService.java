package com.cy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.common.utils.apiUtil.ApiResp;
import com.cy.sys.pojo.entity.SysUser;
import com.cy.sys.pojo.param.user.UserDeleteParam;
import com.cy.sys.pojo.param.user.UserListPageParam;
import com.cy.sys.pojo.param.user.UserSaveParam;
import com.cy.sys.pojo.param.user.UserUpdatePwdParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lil-Kr
 * @since 2020-11-26
 */
public interface ISysUserService extends IService<SysUser> {

    ApiResp edit(UserSaveParam param) throws Exception;

    ApiResp add(UserSaveParam param) throws Exception;

    SysUser findByLoginAccount(String LoginAccount) throws Exception;

    ApiResp delete(UserDeleteParam param) throws Exception;

    ApiResp listPage(UserListPageParam param) throws Exception;

    ApiResp listAll() throws Exception;

    ApiResp updatePassword(UserUpdatePwdParam param) throws Exception;


}
