package com.example.demopet.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.Order;
import com.example.demopet.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
}