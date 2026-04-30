package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.Menu;
import com.example.demopet.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    
    @Transactional(readOnly = true)
    public List<Menu> listWithSort() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
    
    @Transactional(readOnly = true)
    public List<Menu> search(String keyword) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Menu::getMenuName, keyword)
                   .or()
                   .like(Menu::getMenuCode, keyword);
        }
        wrapper.orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
    
    /**
     * 获取树形菜单结构
     */
    @Transactional(readOnly = true)
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = listWithSort();
        return buildMenuTree(allMenus, 0L);
    }
    
    /**
     * 构建树形菜单结构
     */
    private List<Menu> buildMenuTree(List<Menu> allMenus, Long parentId) {
        List<Menu> treeMenus = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子菜单
                List<Menu> children = buildMenuTree(allMenus, menu.getId());
                // 可以通过添加一个临时字段来存储子菜单，但为了简单起见，我们只在需要时查询
                treeMenus.add(menu);
            }
        }
        return treeMenus;
    }
    
    /**
     * 获取父菜单列表（用于表单选择）
     */
    @Transactional(readOnly = true)
    public List<Menu> getParentMenus() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0)
               .or()
               .isNull(Menu::getParentId)
               .orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
    
    /**
     * 根据父ID获取子菜单
     */
    @Transactional(readOnly = true)
    public List<Menu> getChildMenus(Long parentId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, parentId)
               .orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }
}
