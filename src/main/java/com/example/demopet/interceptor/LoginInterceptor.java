package com.example.demopet.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object currentUser = session.getAttribute("currentUser");
        
        if (currentUser == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}
