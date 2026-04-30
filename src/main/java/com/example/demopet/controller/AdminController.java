package com.example.demopet.controller;

import com.example.demopet.entity.Menu;
import com.example.demopet.entity.User;
import com.example.demopet.service.MenuService;
import com.example.demopet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MenuService menuService;
    
    // 管理后台默认首页 - 仪表板
    @GetMapping("")
    public String dashboard(Model model) {
        long userCount = userService.list().size();
        long menuCount = menuService.list().size();
        model.addAttribute("userCount", userCount);
        model.addAttribute("roleCount", 3); // 示例数据，可根据实际角色数量调整
        model.addAttribute("menuCount", menuCount);
        return "admin/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        long userCount = userService.list().size();
        long menuCount = menuService.list().size();
        model.addAttribute("userCount", userCount);
        model.addAttribute("roleCount", 3); // 示例数据，可根据实际角色数量调整
        model.addAttribute("menuCount", menuCount);
        return "admin/dashboard";
    }
    
    // 用户管理模块
    @GetMapping("/users")
    public String usersIndex(Model model, @RequestParam(required = false) String keyword) {
        List<User> userList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            userList = userService.search(keyword);
        } else {
            userList = userService.list();
        }
        model.addAttribute("users", userList);
        model.addAttribute("keyword", keyword);
        return "admin/users/index";
    }
    
    @GetMapping("/users/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "新增");
        return "admin/users/form";
    }
    
    @PostMapping("/users/save")
    public String save(@ModelAttribute User user) {
        if (user.getId() == null) {
            userService.save(user);
        } else {
            userService.update(user);
        }
        return "redirect:/admin/users";
    }
    
    @GetMapping("/users/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("action", "编辑");
        return "admin/users/form";
    }
    
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}
