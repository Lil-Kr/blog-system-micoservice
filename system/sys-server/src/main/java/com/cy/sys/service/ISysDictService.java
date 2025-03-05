package com.cy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.common.utils.apiUtil.ApiResp;
import com.cy.sys.pojo.entity.SysDict;
import com.cy.sys.pojo.param.dict.DictSaveParam;

/**
 * <p>
 *  数据字典服务类
 * </p>
 *
 * @author Lil-Kr
 * @since 2020-11-29
 */
public interface ISysDictService extends IService<SysDict> {

    ApiResp add(DictSaveParam param) throws Exception;

    ApiResp edit(DictSaveParam param) throws Exception;

    ApiResp listAll() throws Exception;
}
