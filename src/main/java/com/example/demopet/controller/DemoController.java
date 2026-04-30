package com.example.demopet.controller;

import com.example.demopet.entity.User;
import com.example.demopet.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DemoController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @RequestMapping("/api/users")
    public List<User> getUsers() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        return userList;
    }
}
