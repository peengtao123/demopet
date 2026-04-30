package com.example.demopet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    private Long id;
    private String orderNo; // 订单号
    private Long userId; // 用户ID
    private BigDecimal totalAmount; // 总金额
    private Integer status; // 状态: 0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消
    private String receiverName; // 收货人姓名
    private String receiverPhone; // 收货人电话
    private String receiverAddress; // 收货地址
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}