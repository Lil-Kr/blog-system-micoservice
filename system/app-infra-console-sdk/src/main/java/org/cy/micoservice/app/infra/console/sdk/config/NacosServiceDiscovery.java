package org.cy.micoservice.app.infra.console.sdk.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.Random;


/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/5
 * @Description: nacos服务发现
 */
public class NacosServiceDiscovery {

    private final NamingService namingService;
    private final Random random = new Random();

    public NacosServiceDiscovery(String serverAddr, String namespace, String user, String pwd) throws NacosException {
        Properties props = new Properties();
        props.put("namespace", namespace);
        props.put("serverAddr", serverAddr);
        props.put("username", user);
        props.put("password", pwd);
        this.namingService = NacosFactory.createNamingService(props);
    }

    /**
     * 获取一个健康实例(随机负载均衡)
     */
    public Instance getRandomHealthyInstance(String serviceName, String group) throws NacosException {
        List<Instance> instances = namingService.selectInstances(serviceName, group, true);
        if (CollectionUtils.isEmpty(instances)) {
            throw new IllegalStateException("No healthy instances for service: " + serviceName);
        }
        // 随机选一个 instance 调用
        return instances.get(random.nextInt(instances.size()));
    }
}