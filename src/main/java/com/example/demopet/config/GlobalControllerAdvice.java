package com.example.demopet.config;

import com.example.demopet.entity.Menu;
import com.example.demopet.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * 全局控制器建议 - 为所有页面提供动态菜单数据
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * 为所有模型添加菜单列表
     */
    @ModelAttribute("menuList")
    public List<Menu> addMenuList(Model model) {
        return menuService.listWithSort();
    }
}
