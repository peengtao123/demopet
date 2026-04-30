package com.example.demopet.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demopet.entity.Product;
import com.example.demopet.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {
}