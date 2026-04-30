package com.example.demopet.controller;

import com.example.demopet.entity.Role;
import com.example.demopet.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {
        List<Role> roleList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            roleList = roleService.search(keyword);
        } else {
            roleList = roleService.list();
        }
        model.addAttribute("roles", roleList);
        model.addAttribute("keyword", keyword);
        return "role/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("action", "新增");
        return "role/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute Role role) {
        roleService.saveOrUpdate(role);
        return "redirect:/role";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        model.addAttribute("action", "编辑");
        return "role/form";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.removeById(id);
        return "redirect:/role";
    }
}
