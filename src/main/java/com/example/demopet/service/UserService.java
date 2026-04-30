package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * 创建或更新测试用户（用于调试）
     */
    @Transactional
    public void createTestUser() {
        // 检查 admin 用户是否存在
        User admin = this.getOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "admin")
        );
        
        if (admin != null) {
            // 更新密码为正确的 BCrypt 哈希
            String newPassword = passwordEncoder.encode("123456");
            admin.setPassword(newPassword);
            this.updateById(admin);
            System.out.println("✅ 已更新 admin 用户密码: " + newPassword);
        } else {
            // 创建新用户
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setPassword(passwordEncoder.encode("123456"));
            newUser.setName("超级管理员");
            newUser.setEmail("admin@example.com");
            newUser.setPhone("13800138000");
            newUser.setStatus(1);
            this.save(newUser);
            System.out.println("✅ 已创建 admin 用户");
        }
    }
}
