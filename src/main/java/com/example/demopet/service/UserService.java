package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public List<User> list() {
        return userMapper.selectList(null);
    }
    
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userMapper.selectById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        userMapper.insert(user);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        userMapper.updateById(user);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<User> search(String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(User::getName, keyword)
                   .or()
                   .like(User::getEmail, keyword);
        }
        return userMapper.selectList(wrapper);
    }
    
    @Transactional(readOnly = true)
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getPassword, password);
        return userMapper.selectOne(wrapper);
    }
}
