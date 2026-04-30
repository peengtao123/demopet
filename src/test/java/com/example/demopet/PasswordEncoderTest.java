package com.example.demopet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具 - 用于生成 BCrypt 密码哈希
 */
public class PasswordEncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成密码 "123456" 的 BCrypt 哈希
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt 哈希: " + encodedPassword);
        System.out.println();
        
        // 验证密码
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + matches);
        
        // 验证旧的哈希值
        String oldHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lqkkO9QS3TzCjH3rS";
        boolean oldMatches = encoder.matches(rawPassword, oldHash);
        System.out.println("旧哈希值验证结果: " + oldMatches);
    }
}
