package org.cy.micoservice.app.gateway;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description:
 */
@Slf4j
public class DubboGenericInvokeTestSuite {

  @Test
  public void doDubboInvoke() {
    RegistryConfig registry = new RegistryConfig();
    registry.setAddress("nacos://192.168.9.200:8848?namespace=blog-mico-service-dev&username=nacos&password=nacos");

    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setName("blog-gateway");

    ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
    reference.setRegistry(registry);

    reference.setGeneric("true");
    reference.setApplication(applicationConfig);
    reference.setInterface("org.cy.micoservice.blog.user.facade.interfaces.UserFacade");
    reference.setProtocol("dubbo");
    reference.setCheck(false);
    GenericService genericService = reference.get();
    Object result = genericService.$invoke("queryUserById", new String[]{"java.lang.Long"}, new Object[]{1l});
    log.info("result -->: {}", result);
  }

  @Test
  public void doDubboInvoke2() {
    RegistryConfig registry = new RegistryConfig();
    registry.setAddress("nacos://192.168.9.200:8848?namespace=blog-mico-service-dev&username=nacos&password=nacos");

    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setName("blog-gateway");

    ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
    reference.setRegistry(registry);

    reference.setGeneric("true");
    reference.setApplication(applicationConfig);
    reference.setInterface("org.cy.micoservice.blog.user.facade.interfaces.UserFacade");
    reference.setProtocol("dubbo");
    reference.setCheck(false);
    GenericService genericService = reference.get();

    JSONObject param = new JSONObject();
    param.put("id", 1l);
    param.put("name", "abc");
    Object result = genericService.$invoke("test", new String[]{"org.cy.micoservice.blog.user.facade.dto.req.TestReq"}, new Object[]{param});
    log.info("result -->: {}", result);
  }
}