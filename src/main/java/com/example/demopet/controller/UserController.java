package com.example.demopet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demopet.entity.Role;
import com.example.demopet.entity.User;
import com.example.demopet.service.RoleService;
import com.example.demopet.service.UserRoleService;
import com.example.demopet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public String userList(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getName, keyword)
                   .or()
                   .like(User::getEmail, keyword)
                   .or()
                   .like(User::getUsername, keyword);
        }
        
        wrapper.orderByDesc(User::getId);
        userService.page(page, wrapper);
        
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "user/index";
    }

    @GetMapping("/form")
    public String userForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            User user = userService.getById(id);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "user/form";
    }

    @PostMapping("/save")
    public String userSave(User user, Model model) {
        // 检查用户名是否已存在
        if (userService.isUsernameExists(user.getUsername(), user.getId())) {
            model.addAttribute("error", "用户名已存在，请使用其他用户名");
            model.addAttribute("user", user);
            return "user/form";
        }
        
        userService.saveOrUpdate(user);
        return "redirect:/user";
    }

    @PostMapping("/delete/{id}")
    public String userDelete(@PathVariable Long id) {
        userService.removeById(id);
        return "redirect:/user";
    }
    
    /**
     * 异步检查用户名是否存在
     */
    @GetMapping("/check-username")
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestParam String username, 
                                            @RequestParam(required = false) Long excludeId) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = userService.isUsernameExists(username, excludeId);
        result.put("exists", exists);
        return result;
    }
    
    /**
     * 显示用户角色分配页面
     */
    @GetMapping("/assign-role/{id}")
    public String assignRoleForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        List<Role> allRoles = roleService.list();
        List<Long> assignedRoleIds = userRoleService.getRoleIdsByUserId(id);
        
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("assignedRoleIds", assignedRoleIds);
        return "user/assign-role";
    }
    
    /**
     * 保存用户角色分配
     */
    @PostMapping("/assign-role/{id}")
    public String assignRoleSave(@PathVariable Long id, @RequestParam(required = false) List<Long> roleIds) {
        userRoleService.assignRolesToUser(id, roleIds);
        return "redirect:/user";
    }
}