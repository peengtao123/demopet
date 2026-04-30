package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.Role;
import com.example.demopet.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    
    @Transactional(readOnly = true)
    public List<Role> search(String keyword) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Role::getRoleName, keyword)
                   .or()
                   .like(Role::getRoleCode, keyword);
        }
        return this.list(wrapper);
    }
}
