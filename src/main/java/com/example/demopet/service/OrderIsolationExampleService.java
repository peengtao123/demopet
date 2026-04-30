package com.example.demopet.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demopet.entity.Order;
import com.example.demopet.entity.OrderItem;
import com.example.demopet.entity.Product;
import com.example.demopet.mapper.OrderItemMapper;
import com.example.demopet.mapper.OrderMapper;
import com.example.demopet.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务 - 演示不同隔离级别的应用
 */
@Service
public class OrderIsolationExampleService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 场景1：创建订单 - 使用默认隔离级别
     * 大多数业务场景使用 DEFAULT 即可（MySQL 下是 REPEATABLE_READ）
     */
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Order createOrder(Order order, List<OrderItem> items) {
        // 1. 保存订单
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);
        
        // 2. 保存订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        
        return order;
    }
    
    /**
     * 场景2：查询订单列表 - 使用读已提交
     * 报表查询不需要严格一致性，READ_COMMITTED 性能更好
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<Order> queryOrdersWithReadCommitted() {
        // 只读查询，使用较低的隔离级别提升并发性能
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }
    
    /**
     * 场景3：统计订单数据 - 使用读已提交 + 只读
     * 统计数据允许轻微的不一致，追求查询性能
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public OrderStatistics getOrderStatistics() {
        // 统计总订单数
        long totalOrders = orderMapper.selectCount(null);
        
        // 统计总金额
        List<Order> orders = orderMapper.selectList(null);
        BigDecimal totalAmount = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new OrderStatistics(totalOrders, totalAmount);
    }
    
    /**
     * 场景4：扣减库存并创建订单 - 使用可重复读
     * 库存操作需要保证一致性，避免超卖
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public Order createOrderWithStockDeduct(Order order, List<OrderItem> items) {
        // 1. 检查并扣减库存
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            
            if (product == null) {
                throw new RuntimeException("商品不存在：" + item.getProductId());
            }
            
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("库存不足：" + product.getProductName());
            }
            
            // 扣减库存
            product.setStock(product.getStock() - item.getQuantity());
            product.setSales(product.getSales() + item.getQuantity());
            productMapper.updateById(product);
        }
        
        // 2. 创建订单
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);
        
        // 3. 创建订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        
        return order;
    }
    
    /**
     * 场景5：高并发库存扣减 - 使用串行化（谨慎使用）
     * 极端情况下使用，完全避免并发问题
     * 注意：会导致严重的性能问题，仅用于关键业务
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deductStockSerializably(Long productId, int quantity) {
        Product product = productMapper.selectById(productId);
        
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        // 扣减库存
        product.setStock(product.getStock() - quantity);
        productMapper.updateById(product);
        
        // 在 SERIALIZABLE 级别下，其他事务必须等待这个事务完成
        // 这确保了不会出现超卖，但会严重影响并发性能
    }
    
    /**
     * 场景6：批量更新订单状态 - 使用默认隔离级别
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateOrderStatus(List<Long> orderIds, Integer status) {
        for (Long orderId : orderIds) {
            Order order = orderMapper.selectById(orderId);
            if (order != null) {
                order.setStatus(status);
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
            }
        }
    }
    
    /**
     * 场景7：取消订单并恢复库存 - 使用可重复读
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void cancelOrderAndRestoreStock(Long orderId) {
        // 1. 查询订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        // 2. 更新订单状态
        order.setStatus(4); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        // 3. 恢复库存
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                product.setSales(product.getSales() - item.getQuantity());
                productMapper.updateById(product);
            }
        }
    }
    
    /**
     * 内部类：订单统计数据
     */
    public static class OrderStatistics {
        private long totalOrders;
        private BigDecimal totalAmount;
        
        public OrderStatistics(long totalOrders, BigDecimal totalAmount) {
            this.totalOrders = totalOrders;
            this.totalAmount = totalAmount;
        }
        
        public long getTotalOrders() {
            return totalOrders;
        }
        
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }
    }
}
