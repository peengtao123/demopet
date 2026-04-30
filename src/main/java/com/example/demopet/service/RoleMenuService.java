package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.RoleMenu;
import com.example.demopet.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleMenuService extends ServiceImpl<RoleMenuMapper, RoleMenu> {
    
    /**
     * 根据角色ID获取菜单ID列表
     */
    @Transactional(readOnly = true)
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = this.list(wrapper);
        return roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
    
    /**
     * 为角色分配菜单权限（先删除旧关系，再添加新关系）
     */
    @Transactional
    public void assignMenusToRole(Long roleId, List<Long> menuIds) {
        // 删除角色原有的所有菜单权限
        LambdaQueryWrapper<RoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RoleMenu::getRoleId, roleId);
        this.remove(deleteWrapper);
        
        // 添加新的菜单权限关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                this.save(roleMenu);
            }
        }
    }
}
