package com.example.demopet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demopet.entity.User;
import com.example.demopet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
    public String userSave(User user) {
        userService.saveOrUpdate(user);
        return "redirect:/user";
    }

    @PostMapping("/delete/{id}")
    public String userDelete(@PathVariable Long id) {
        userService.removeById(id);
        return "redirect:/user";
    }
}