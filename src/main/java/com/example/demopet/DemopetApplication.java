package com.example.demopet;

import com.example.demopet.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demopet.mapper")
public class DemopetApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(DemopetApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 应用启动后，确保 admin 用户密码正确
		userService.createTestUser();
	}
}
