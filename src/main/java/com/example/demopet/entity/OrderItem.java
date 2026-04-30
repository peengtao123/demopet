package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productCode;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private LocalDateTime createTime;
}
