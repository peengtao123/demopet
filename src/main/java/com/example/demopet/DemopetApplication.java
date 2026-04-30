package com.example.demopet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demopet.mapper")
public class DemopetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemopetApplication.class, args);
	}

}
