package com.cy.auth.filter;

import com.cy.auth.common.constant.InterceptorName;
import com.cy.auth.common.holder.RequestHolder;
import com.cy.auth.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description: 拦截需要用户登录的请求
 * @Author: Lil-K
 * @Date: 2020/11/30
 */
//@WebFilter(filterName="userLogin", urlPatterns = {"/auth/*","/admin/*"})
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 获取请求路径
        String servletPath = req.getServletPath();

        // 获取用户信息
        User user = (User)req.getSession().getAttribute(InterceptorName.userInfo);

        if (Objects.isNull(user)) {// 用户未登录
            log.warn("用户未登录");
        }else {// 用户登录就添加用户登录信息
            log.warn("用户登录成功");
            RequestHolder.setCurrentUser(user);
            RequestHolder.setHttpServletRequest(req);
        }
        filterChain.doFilter(request,response);
        return;
    }

    @Override
    public void destroy() {

    }
}
