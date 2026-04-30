package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.UserRole;
import com.example.demopet.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {
    
    /**
     * 根据用户ID获取角色ID列表
     */
    @Transactional(readOnly = true)
    public List<Long> getRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = this.list(wrapper);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }
    
    /**
     * 为用户分配角色（先删除旧关系，再添加新关系）
     */
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // 删除用户原有的所有角色
        LambdaQueryWrapper<UserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRole::getUserId, userId);
        this.remove(deleteWrapper);
        
        // 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                this.save(userRole);
            }
        }
    }
}
