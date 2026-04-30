package com.example.demopet.controller;

import com.example.demopet.entity.Menu;
import com.example.demopet.entity.Role;
import com.example.demopet.service.MenuService;
import com.example.demopet.service.RoleMenuService;
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
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RoleMenuService roleMenuService;
    
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
    
    /**
     * 显示角色菜单权限分配页面
     */
    @GetMapping("/assign-menu/{id}")
    public String assignMenuForm(@PathVariable Long id, Model model) {
        Role role = roleService.getById(id);
        // 获取所有菜单（平铺列表，按排序字段排序）
        List<Menu> allMenus = menuService.listWithSort();
        List<Long> assignedMenuIds = roleMenuService.getMenuIdsByRoleId(id);
        
        model.addAttribute("role", role);
        model.addAttribute("allMenus", allMenus);
        model.addAttribute("assignedMenuIds", assignedMenuIds);
        return "role/assign-menu";
    }
    
    /**
     * 保存角色菜单权限分配
     */
    @PostMapping("/assign-menu/{id}")
    public String assignMenuSave(@PathVariable Long id, @RequestParam(required = false) List<Long> menuIds) {
        roleMenuService.assignMenusToRole(id, menuIds);
        return "redirect:/role";
    }
}
