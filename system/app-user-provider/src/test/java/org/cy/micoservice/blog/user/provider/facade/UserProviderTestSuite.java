package org.cy.micoservice.blog.user.provider.facade;

import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Slf4j
public class UserProviderTestSuite {

  // 应用配置
  private ApplicationConfig application;
  // 注册中心配置
  private RegistryConfig registry;

  @Before
  public void setUp() {
    application = new ApplicationConfig();
    application.setName("blog-user-consumer-test");

    registry = new RegistryConfig();
    registry.setAddress("nacos://192.168.9.200:8848?namespace=blog-mico-service-dev&username=nacos&&password=nacos");
  }

  @Test
  public void testDubboGlobalExceptionFilter() {
    // 引用远程服务
    ReferenceConfig<UserFacade> reference = new ReferenceConfig<>();
    reference.setApplication(application);
    reference.setRegistry(registry);
    reference.setInterface(UserFacade.class);
//    reference.setGroup("test-group");
    reference.setAsync(false);
    reference.setTimeout(10000);

    // 获取远程服务代理
    UserFacade userFacade = reference.get();
    try {
      SysUserDTO userDTO = userFacade.getUserBySurrogateId(null);
      log.info("userDTO: {}", JSONArray.toJSONString(userDTO));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}