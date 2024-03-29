package com.cy.security.authconfig;

import com.cy.security.utils.secret.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author: Lil-K
 * @Date: 2022/12/19
 * @Description:
 */
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SuccessLoginHandler successLoginHandler;

    @Autowired
    private FailureLoginHandler failureLoginHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

//    @Autowired
//    @Qualifier("customLoginAdminAuthFilter")
//    private CustomLoginAdminAuthFilter customLoginAdminAuthFilter;

//    @Autowired
//    @Qualifier("loginAdminFilter")
//    private LoginAdminFilter loginAdminFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5Encoder();
    }

    /**
     * 配置全局的AuthenticationManager
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    LoginAdminFilter loginFilter(AuthenticationManager authenticationManager) throws Exception {
//        LoginAdminFilter loginFilter = new LoginAdminFilter(authenticationManager);
//        loginFilter.setFilterProcessesUrl("/login");
//        loginFilter.setUsernameParameter("username");
//        loginFilter.setPasswordParameter("password");
////        loginFilter.setAuthenticationManager();
//        loginFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
//        loginFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
//        return loginFilter;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 所有请求都需要校验
                .authorizeRequests()
                // 配置放行的api
                .antMatchers("/admin/login").permitAll()
                // 其他url请求都需要验证
                .anyRequest().authenticated()
                .and()
                // 关闭csrf
                .csrf()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .addFilterBefore(loginAdminFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(customLoginAdminAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic();

        return http.build();
    }

}