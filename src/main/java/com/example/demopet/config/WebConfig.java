package com.example.demopet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 注：登录验证已由 Spring Security 处理，不再需要自定义拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring Security 已处理认证和授权，无需额外配置
}
