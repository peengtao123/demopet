package com.example.demopet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demopet.entity.Order;
import com.example.demopet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String orderNo,
                       @RequestParam(required = false) Integer status,
                       Model model) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        orderService.page(page, wrapper);
        
        model.addAttribute("page", page);
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("status", status);
        return "order/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        return "order/detail";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(status);
            order.setUpdateTime(LocalDateTime.now());
            orderService.updateById(order);
        }
        return "redirect:/order";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        orderService.removeById(id);
        return "redirect:/order";
    }
}