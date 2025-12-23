package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Lil-K
 * @Date: Created at 2025/9/29
 * @Description: 网关路由配置保存请求体
 */
@Data
public class RouteConfigSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1743189351674108284L;

    private String schema;

    private String method;

    private String path;

    private String uri;

    private String authType;
}
