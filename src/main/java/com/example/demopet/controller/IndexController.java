package com.example.demopet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index(Authentication authentication) {
        // 如果已登录，重定向到仪表板
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        }
        // 否则显示欢迎页面
        return "index";
    }
}