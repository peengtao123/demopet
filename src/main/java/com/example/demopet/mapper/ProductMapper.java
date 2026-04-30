package com.example.demopet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demopet.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}