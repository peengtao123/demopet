package com.example.demopet.controller;

import com.example.demopet.entity.Menu;
import com.example.demopet.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {
        List<Menu> menuList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            menuList = menuService.search(keyword);
        } else {
            menuList = menuService.list();
        }
        model.addAttribute("menus", menuList);
        model.addAttribute("keyword", keyword);
        return "admin/menu/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("action", "新增");
        return "admin/menu/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute Menu menu) {
        if (menu.getId() == null) {
            menuService.save(menu);
        } else {
            menuService.update(menu);
        }
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("action", "编辑");
        return "admin/menu/form";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        menuService.delete(id);
        return "redirect:/admin/menu";
    }
}
