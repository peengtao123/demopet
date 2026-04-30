package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demopet.entity.Role;
import com.example.demopet.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Transactional(readOnly = true)
    public List<Role> list() {
        return roleMapper.selectList(null);
    }
    
    @Transactional(readOnly = true)
    public Role getById(Long id) {
        return roleMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void save(Role role) {
        roleMapper.insert(role);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void update(Role role) {
        roleMapper.updateById(role);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        roleMapper.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Role> search(String keyword) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Role::getRoleName, keyword)
                   .or()
                   .like(Role::getRoleCode, keyword);
        }
        return roleMapper.selectList(wrapper);
    }
}
