# Spring Boot 事务管理完整指南

## 📋 目录
- [1. 事务基础概念](#1-事务基础概念)
- [2. @Transactional 注解详解](#2-transactional-注解详解)
- [3. 实际应用场景](#3-实际应用场景)
- [4. 最佳实践](#4-最佳实践)
- [5. 常见问题与解决方案](#5-常见问题与解决方案)

---

## 1. 事务基础概念

### 1.1 什么是事务？
事务是数据库操作的最小工作单元，具有以下 **ACID** 特性：

- **A (Atomicity) 原子性**：事务中的所有操作要么全部成功，要么全部失败回滚
- **C (Consistency) 一致性**：事务执行前后，数据库必须保持一致性状态
- **I (Isolation) 隔离性**：多个事务并发执行时，互不干扰
- **D (Durability) 持久性**：事务一旦提交，对数据的修改是永久的

### 1.2 并发事务带来的问题

在多个事务同时执行时，可能会出现以下问题：

| 问题 | 说明 | 示例 |
|------|------|------|
| **脏读（Dirty Read）** | 读取到其他事务未提交的数据 | 事务A修改数据但未提交，事务B读取了这个修改 |
| **不可重复读（Non-repeatable Read）** | 同一事务中多次读取同一数据，结果不一致 | 事务A第一次读取后，事务B修改并提交了数据，事务A再次读取得到不同结果 |
| **幻读（Phantom Read）** | 同一查询条件下，多次查询返回的记录数不同 | 事务A查询后，事务B插入新记录并提交，事务A再次查询发现多了记录 |

### 1.3 事务隔离级别

SQL标准定义了4种隔离级别，从低到高：

| 隔离级别 | 脏读 | 不可重复读 | 幻读 | 性能 | 说明 |
|---------|------|-----------|------|------|------|
| **READ_UNCOMMITTED** | ❌ 可能 | ❌ 可能 | ❌ 可能 | ⚡⚡⚡ 最快 | 读取未提交的数据 |
| **READ_COMMITTED** | ✅ 避免 | ❌ 可能 | ❌ 可能 | ⚡⚡ 快 | 只能读取已提交的数据 |
| **REPEATABLE_READ** | ✅ 避免 | ✅ 避免 | ❌ 可能 | ⚡ 中等 | 默认级别（MySQL） |
| **SERIALIZABLE** | ✅ 避免 | ✅ 避免 | ✅ 避免 | 🐢 最慢 | 完全串行化执行 |

#### READ_UNCOMMITTED（读未提交）
```java
// 最低隔离级别，允许读取其他事务未提交的数据
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public void readUncommittedExample() {
    // 可能读取到其他事务未提交的"脏数据"
}
```
**问题**：如果其他事务回滚，你读取的数据就是无效的。

#### READ_COMMITTED（读已提交）
```java
// Oracle 默认级别，只能读取已提交的数据
@Transactional(isolation = Isolation.READ_COMMITTED)
public void readCommittedExample() {
    // 确保读取的数据都是其他事务已经提交的
}
```
**问题**：同一事务中多次读取可能得到不同结果（不可重复读）。

#### REPEATABLE_READ（可重复读）⭐ MySQL默认
```java
// MySQL 默认级别，保证同一事务中多次读取结果一致
@Transactional(isolation = Isolation.REPEATABLE_READ)
public void repeatableReadExample() {
    // 第一次读取后，其他事务的修改不会影响本次读取结果
}
```
**问题**：可能出现幻读（其他事务插入新记录）。

#### SERIALIZABLE（串行化）
```java
// 最高隔离级别，完全避免并发问题
@Transactional(isolation = Isolation.SERIALIZABLE)
public void serializableExample() {
    // 事务按顺序执行，完全没有并发问题
    // 但性能最差，可能导致大量锁等待
}
```
**优点**：完全避免所有并发问题。
**缺点**：性能最差，可能导致大量超时。

### 1.4 Spring 中的隔离级别设置

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    /**
     * 使用数据库默认隔离级别（推荐）
     */
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        orderMapper.insert(order);
        return order;
    }
    
    /**
     * 指定读已提交隔离级别
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<Order> queryOrders() {
        return orderMapper.selectList(null);
    }
    
    /**
     * 高并发场景使用串行化（谨慎使用）
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateStock(Long productId, int quantity) {
        // 扣减库存操作
        Product product = productMapper.selectById(productId);
        product.setStock(product.getStock() - quantity);
        productMapper.updateById(product);
    }
}
```

### 1.5 如何选择隔离级别？

| 场景 | 推荐隔离级别 | 原因 |
|------|-------------|------|
| **大多数业务** | `DEFAULT`（使用数据库默认） | 平衡性能和一致性 |
| **报表查询** | `READ_COMMITTED` | 不需要严格一致性，追求性能 |
| **金融交易** | `REPEATABLE_READ` 或 `SERIALIZABLE` | 需要强一致性 |
| **库存扣减** | `REPEATABLE_READ` + 乐观锁 | 避免超卖 |
| **高并发读** | `READ_COMMITTED` | 减少锁竞争 |

### 1.6 Spring 事务管理类型

| 类型 | 说明 | 使用场景 |
|------|------|----------|
| **声明式事务** | 通过 `@Transactional` 注解实现 | ✅ 推荐使用，代码简洁 |
| **编程式事务** | 通过 `TransactionTemplate` 手动控制 | 需要精细控制事务边界 |

---

## 2. @Transactional 注解详解

### 2.1 基本用法

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // 基本事务 - 任何 RuntimeException 都会触发回滚
    @Transactional
    public void createUser(User user) {
        userMapper.insert(user);
    }
}
```

### 2.2 常用属性

#### rollbackFor - 指定回滚异常类型
```java
// 所有异常都回滚（推荐）
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    userMapper.insert(user);
}

// 仅运行时异常回滚（默认行为）
@Transactional
public void saveUser(User user) {
    userMapper.insert(user);
}
```

#### noRollbackFor - 指定不回滚的异常
```java
@Transactional(
    rollbackFor = Exception.class, 
    noRollbackFor = IllegalArgumentException.class
)
public void saveUser(User user) {
    userMapper.insert(user);
    // IllegalArgumentException 不会触发回滚
}
```

#### readOnly - 只读事务（优化查询性能）
```java
@Transactional(readOnly = true)
public User getUserById(Long id) {
    return userMapper.selectById(id);
}
```

#### timeout - 超时设置（秒）
```java
@Transactional(timeout = 30)
public void batchInsert(List<User> users) {
    // 如果执行超过30秒，自动回滚
    for (User user : users) {
        userMapper.insert(user);
    }
}
```

### 2.3 事务传播行为（Propagation）

| 传播行为 | 说明 | 使用场景 |
|---------|------|----------|
| **REQUIRED** | 如果当前有事务就加入，没有就创建新事务（默认） | ✅ 最常用 |
| **REQUIRES_NEW** | 无论是否有事务，都创建新事务 | 日志记录、独立操作 |
| **NESTED** | 嵌套事务，可以独立回滚 | 部分回滚场景 |
| **SUPPORTS** | 支持当前事务，没有就以非事务方式执行 | 查询操作 |
| **NOT_SUPPORTED** | 以非事务方式执行，如果有事务则挂起 | 批量处理 |
| **MANDATORY** | 必须在事务中执行，否则抛异常 | 强制事务场景 |
| **NEVER** | 以非事务方式执行，如果有事务则抛异常 | 禁止事务场景 |

#### REQUIRED（默认）
```java
@Transactional(propagation = Propagation.REQUIRED)
public void methodA() {
    // 如果调用方有事务，加入该事务
    // 如果没有事务，创建新事务
    userMapper.insert(user);
}
```

#### REQUIRES_NEW
```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void methodB() {
    // 总是创建新事务，与外部事务无关
    // 即使外部事务回滚，这个方法的操作也不会回滚
    logMapper.insert(log);
}
```

#### NESTED
```java
@Transactional(propagation = Propagation.NESTED)
public void methodC() {
    // 嵌套事务，可以独立回滚
    // 但外部事务回滚会影响这个方法
    orderItemMapper.insert(item);
}
```

---

## 3. 实际应用场景

### 场景 1：用户注册（多表操作）

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    /**
     * 用户注册 - 同时创建用户和分配角色
     * 任何一步失败，整个操作回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(User user, Long roleId) {
        // 1. 保存用户
        userMapper.insert(user);
        
        // 2. 分配角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
        
        // 如果这里抛出异常，上面的操作都会回滚
    }
}
```

### 场景 2：订单创建（复杂业务）

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 创建订单 - 扣减库存、创建订单、创建订单项
     */
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order, List<OrderItem> items) {
        // 1. 检查并扣减库存
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("库存不足：" + product.getProductName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productMapper.updateById(product);
        }
        
        // 2. 创建订单
        orderMapper.insert(order);
        
        // 3. 创建订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        
        return order;
    }
}
```

### 场景 3：权限分配（先删后增）

```java
@Service
public class UserRoleService {
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    /**
     * 为用户分配角色 - 先删除旧关系，再添加新关系
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // 1. 删除用户原有的所有角色
        LambdaQueryWrapper<UserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRole::getUserId, userId);
        userRoleMapper.delete(deleteWrapper);
        
        // 2. 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        
        // 如果添加新角色失败，删除操作也会回滚
    }
}
```

### 场景 4：批量操作

```java
@Service
public class ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 批量导入商品
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchImportProducts(List<Product> products) {
        int count = 0;
        for (Product product : products) {
            // 验证商品数据
            if (product.getPrice() <= 0) {
                throw new RuntimeException("商品价格必须大于0：" + product.getProductName());
            }
            
            productMapper.insert(product);
            count++;
            
            // 每100条提交一次（可选优化）
            if (count % 100 == 0) {
                // 可以在这里刷新会话
            }
        }
    }
}
```

### 场景 5：查询优化（只读事务）

```java
@Service
public class ReportService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 生成报表 - 大量查询操作
     * readOnly=true 可以提升性能
     */
    @Transactional(readOnly = true)
    public ReportData generateReport(LocalDate startDate, LocalDate endDate) {
        // 多个查询共享同一个只读事务
        List<Order> orders = orderMapper.selectByDateRange(startDate, endDate);
        List<User> users = userMapper.selectActiveUsers();
        
        // 处理数据...
        return new ReportData(orders, users);
    }
}
```

---

## 4. 最佳实践

### ✅ 推荐做法

#### 1. 始终指定 rollbackFor
```java
// ✅ 推荐
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    // ...
}

// ❌ 不推荐 - 只有 RuntimeException 会回滚
@Transactional
public void saveUser(User user) {
    // ...
}
```

#### 2. 在 Service 层使用事务
```java
// ✅ 正确 - 在 Service 层
@Service
public class UserService {
    @Transactional(rollbackFor = Exception.class)
    public void createUser(User user) {
        // ...
    }
}

// ❌ 错误 - 不要在 Controller 层使用事务
@Controller
public class UserController {
    @Transactional  // 不推荐
    public String createUser(User user) {
        // ...
    }
}
```

#### 3. 保持事务方法简短
```java
// ✅ 推荐 - 事务方法只做必要的数据库操作
@Transactional(rollbackFor = Exception.class)
public void createUser(User user) {
    userMapper.insert(user);
    userRoleMapper.insert(userRole);
}

// ❌ 不推荐 - 事务中包含耗时操作
@Transactional(rollbackFor = Exception.class)
public void createUser(User user) {
    userMapper.insert(user);
    sendEmail(user);      // 耗时操作
    callExternalApi();    // 网络请求
    generatePDF();        // 文件IO
}
```

#### 4. 查询使用只读事务
```java
// ✅ 推荐
@Transactional(readOnly = true)
public User getUserById(Long id) {
    return userMapper.selectById(id);
}
```

#### 5. 避免大事务
```java
// ✅ 推荐 - 拆分事务
@Transactional(rollbackFor = Exception.class)
public void step1() {
    // 第一步操作
}

@Transactional(rollbackFor = Exception.class)
public void step2() {
    // 第二步操作
}

// ❌ 不推荐 - 一个大事务包含所有操作
@Transactional(rollbackFor = Exception.class)
public void doEverything() {
    // 很多操作...
}
```

### ❌ 常见错误

#### 1. 自调用导致事务失效
```java
@Service
public class UserService {
    
    // ❌ 错误 - 同类方法调用，事务不生效
    public void createUser(User user) {
        // 直接调用，@Transactional 不会生效
        this.saveUser(user);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        userMapper.insert(user);
    }
}

// ✅ 正确 - 通过注入自身或其他方式
@Service
public class UserService {
    
    @Autowired
    private UserService self;  // 注入自身
    
    public void createUser(User user) {
        // 通过代理调用，事务生效
        self.saveUser(user);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
        userMapper.insert(user);
    }
}
```

#### 2. 异常被捕获导致事务不生效
```java
// ❌ 错误 - 异常被捕获，事务不会回滚
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    try {
        userMapper.insert(user);
        throw new RuntimeException("模拟异常");
    } catch (Exception e) {
        log.error("错误", e);
        // 异常被捕获，事务不会回滚
    }
}

// ✅ 正确 - 重新抛出异常
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    try {
        userMapper.insert(user);
        throw new RuntimeException("模拟异常");
    } catch (Exception e) {
        log.error("错误", e);
        throw e;  // 重新抛出，触发回滚
    }
}
```

#### 3. 方法不是 public
```java
// ❌ 错误 - private 方法事务不生效
@Transactional(rollbackFor = Exception.class)
private void saveUser(User user) {
    userMapper.insert(user);
}

// ✅ 正确 - 必须是 public
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    userMapper.insert(user);
}
```

---

## 5. 常见问题与解决方案

### Q1: 事务为什么不生效？

**可能原因：**
1. 方法不是 `public`
2. 同类内部调用（自调用）
3. 异常被捕获没有抛出
4. 数据库引擎不支持事务（如 MyISAM）
5. 没有配置事务管理器

**解决方案：**
```java
// 确保方法是 public
@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    // ...
}

// 确保使用 InnoDB 引擎
// CREATE TABLE user (...) ENGINE=InnoDB;
```

### Q2: 如何手动控制事务回滚？

```java
@Transactional(rollbackFor = Exception.class)
public void saveWithManualRollback(User user, boolean shouldRollback) {
    userMapper.insert(user);
    
    if (shouldRollback) {
        // 手动设置回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
```

### Q3: 如何在事务中执行非事务操作？

```java
@Service
public class LogService {
    
    @Autowired
    private LogMapper logMapper;
    
    /**
     * 记录日志 - 使用新事务，不受外部事务影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveLog(String message) {
        Log log = new Log();
        log.setMessage(message);
        log.setTime(LocalDateTime.now());
        logMapper.insert(log);
        // 即使外部事务回滚，日志也会保存
    }
}
```

### Q4: 如何处理分布式事务？

对于跨服务、跨数据库的场景，建议使用：
- **Seata** - 分布式事务解决方案
- **消息队列最终一致性** - 通过 MQ 保证最终一致
- **TCC 模式** - Try-Confirm-Cancel

---

## 📚 参考资源

- [Spring 官方文档 - 事务管理](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
- [MyBatis-Plus 事务支持](https://baomidou.com/pages/4b4bc6/)
- [MySQL 事务隔离级别](https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-isolation-levels.html)

---

## 💡 总结

1. **始终使用 `@Transactional(rollbackFor = Exception.class)`**
2. **在 Service 层使用事务，保持方法简短**
3. **查询操作使用 `readOnly = true` 优化性能**
4. **注意事务失效的常见陷阱**
5. **根据业务需求选择合适的事务传播行为**

---

**最后更新**: 2026-04-30  
**项目**: demopet - Spring Boot 电商后台管理系统
