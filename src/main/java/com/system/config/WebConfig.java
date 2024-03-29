package com.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.system.utils.interceptor.LoginHandlerInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

	 /**
     * 注册 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
        		.excludePathPatterns("/**/*rpc*/**")
                .excludePathPatterns("**/unauth/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/login")
                .excludePathPatterns("/toIndex");

    }

}