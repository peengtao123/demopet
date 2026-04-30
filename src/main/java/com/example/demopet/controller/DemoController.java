package com.example.demopet.controller;

import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String index() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        return "Greetings from Spring Boot!";
    }
}
