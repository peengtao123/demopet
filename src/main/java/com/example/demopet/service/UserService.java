package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    @Transactional(readOnly = true)
    public List<User> search(String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(User::getName, keyword)
                   .or()
                   .like(User::getEmail, keyword)
                   .or()
                   .like(User::getUsername, keyword);
        }
        return this.list(wrapper);
    }
    
    @Transactional(readOnly = true)
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getPassword, password);
        return this.getOne(wrapper);
    }
    
    /**
     * 检查用户名是否已存在
     * @param username 要检查的用户名
     * @param excludeId 排除的用户ID（用于编辑时排除当前用户）
     * @return true-用户名已存在，false-用户名可用
     */
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username, Long excludeId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (excludeId != null) {
            wrapper.ne(User::getId, excludeId);
        }
        return this.count(wrapper) > 0;
    }
}
