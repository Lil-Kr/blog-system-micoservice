package org.cy.micoservice.blog.framework.dubbo.starter.auth.service.impl;

import org.cy.micoservice.blog.admin.dao.SysDictDetailMapper;
import org.cy.micoservice.blog.admin.service.SysDictService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictDetailResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class SysDictServiceImplTest {

	@Autowired
	private SysDictDetailMapper dictDetailMapper;

	@Autowired
	private SysDictService dictService;

	@Test
	public void test1() {
		ApiResp<Map<String, List<SysDictDetailResp>>> mapApiResp = dictService.dictDetailMapping();
		Map<String, List<SysDictDetailResp>> data = mapApiResp.getData();
	}

}