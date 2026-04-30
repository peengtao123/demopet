package com.example.demopet.config;

import com.example.demopet.entity.Menu;
import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import com.example.demopet.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * 全局控制器建议 - 为所有页面提供动态菜单数据和当前用户信息
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 为所有模型添加菜单列表
     */
    @ModelAttribute("menuList")
    public List<Menu> addMenuList(Model model) {
        return menuService.listWithSort();
    }
    
    /**
     * 为所有模型添加当前用户信息
     */
    @ModelAttribute("currentUser")
    public User addCurrentUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            return userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                    .eq(User::getUsername, username)
            );
        }
        return null;
    }
}
