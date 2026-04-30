package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    private Long id;
    private String productName; // 商品名称
    private String productCode; // 商品编码
    private BigDecimal price; // 价格
    private Integer stock; // 库存
    private Integer sales; // 销量
    private String description; // 描述
    private String imageUrl; // 图片URL
    private Integer status; // 状态: 0-下架, 1-上架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
