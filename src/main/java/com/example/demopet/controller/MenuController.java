package com.example.demopet.controller;

import com.example.demopet.entity.Menu;
import com.example.demopet.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {
        List<Menu> menuList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            menuList = menuService.search(keyword);
        } else {
            menuList = menuService.listWithSort();
        }
        model.addAttribute("menus", menuList);
        model.addAttribute("keyword", keyword);
        return "menu/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("action", "新增");
        // 获取父菜单列表用于选择
        model.addAttribute("parentMenus", menuService.getParentMenus());
        return "menu/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute Menu menu) {
        // 如果parentId为空或0，则设置为顶级菜单
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        // 设置默认值
        if (menu.getMenuType() == null) {
            menu.setMenuType(2); // 默认为菜单类型
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1); // 默认启用
        }
        if (menu.getSort() == null) {
            menu.setSort(0);
        }
        menuService.saveOrUpdate(menu);
        return "redirect:/menu";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("action", "编辑");
        // 获取父菜单列表用于选择（排除当前菜单及其子菜单）
        model.addAttribute("parentMenus", menuService.getParentMenus());
        return "menu/form";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        menuService.removeById(id);
        return "redirect:/menu";
    }
}
