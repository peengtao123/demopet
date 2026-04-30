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
}
