package com.example.demopet.config;

import com.example.demopet.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private LoginInterceptor loginInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/dashboard", "/user/**", "/role/**", "/menu/**", "/product/**", "/order/**")  // 拦截所有后台管理页面
                .excludePathPatterns("/login", "/", "/error"); // 排除登录页面和错误页面
    }
}
