package com.cy.sys.config;

import com.alibaba.fastjson2.JSON;
import com.cy.common.utils.apiUtil.ApiResp;
import com.cy.sys.common.constant.InterceptorName;
import com.cy.sys.common.holder.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


/**
 * 拦截器 校验前端参数
 * @author Lil-Kr
 * @since 2020-11-12
 */
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    /**
     * 拦截请求，在controller调用之前
     * 返回 false：请求被拦截，返回
     * 返回 true ：请求OK，可以继续执行，放行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String url = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("request start. url={}, params={}",url, JSON.toJSONString(parameterMap));
        long start = System.currentTimeMillis();
        request.setAttribute(InterceptorName.startTime,start);
        return true;
    }

    /**
     * 把拦截数据返回给前端
     * @param response
     * @throws IOException
     */
    private void returnErrorResponse(HttpServletResponse response, ApiResp<String> apiResp) throws IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding("utf-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(apiResp.toString().getBytes("utf-8"));
            out.flush();
        } finally {
            if(out!=null){
                out.close();
            }
        }
    }

    /**
     * 请求controller之后，渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv) throws Exception {
        
    }

    /**
     * 请求controller之后，视图渲染之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
        String url = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        long start = (Long) request.getAttribute(InterceptorName.startTime);
        long end = System.currentTimeMillis();
        log.info("request start. url={}, cost={}",url, end-start);

        // 将用户信息和请求信息添加到缓存
        RequestHolder.remove();
    }

    /**
     * 移除ThreadLocal中的信息
     */
    public void removeThreadLocalInfo(){
        RequestHolder.removeHttpServletRequest();
        RequestHolder.removeUser();
    }
}
