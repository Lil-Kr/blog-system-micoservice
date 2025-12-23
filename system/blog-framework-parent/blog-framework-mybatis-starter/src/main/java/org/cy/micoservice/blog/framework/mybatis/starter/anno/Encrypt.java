package org.cy.micoservice.blog.framework.mybatis.starter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/7/20
 * @Description: 标记需要做加密处理的字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  Encrypt {
}
