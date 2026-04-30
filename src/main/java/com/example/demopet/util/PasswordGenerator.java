package com.example.demopet.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密码生成工具
 * 运行此类的 main 方法可以生成密码的 BCrypt 哈希值
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 要加密的原始密码
        String rawPassword = "123456";
        
        // 生成 BCrypt 哈希（每次运行结果都不同，但都能验证通过）
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("========================================");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt 哈希: " + encodedPassword);
        System.out.println("========================================");
        System.out.println();
        
        // 验证生成的哈希是否正确
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + (matches ? "✓ 成功" : "✗ 失败"));
        System.out.println();
        
        // 生成 SQL 更新语句
        System.out.println("SQL 更新语句：");
        System.out.println("UPDATE user SET password = '" + encodedPassword + "' WHERE username = 'admin';");
        System.out.println();
        
        // 生成完整的 INSERT 语句
        System.out.println("完整的数据初始化 SQL：");
        System.out.println("INSERT INTO `user` (id, username, password, name, email, phone, status) VALUES");
        System.out.println("(1, 'admin', '" + encodedPassword + "', '超级管理员', 'admin@example.com', '13800138000', 1),");
        System.out.println("(2, 'manager', '" + encodedPassword + "', '管理员', 'manager@example.com', '13800138001', 1),");
        System.out.println("(3, 'user1', '" + encodedPassword + "', '张三', 'zhangsan@example.com', '13800138002', 1),");
        System.out.println("(4, 'user2', '" + encodedPassword + "', '李四', 'lisi@example.com', '13800138003', 1),");
        System.out.println("(5, 'user3', '" + encodedPassword + "', '王五', 'wangwu@example.com', '13800138004', 1);");
    }
}
