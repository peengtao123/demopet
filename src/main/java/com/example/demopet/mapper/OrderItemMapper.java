package com.example.demopet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demopet.entity.OrderItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    
    /**
     * 根据订单ID查询订单项
     */
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(Long orderId);
}
