package com.example.demopet.service;

import com.example.demopet.entity.Role;
import com.example.demopet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务管理示例服务类
 * 演示Spring Boot中各种事务场景的使用
 */
@Service
public class TransactionExampleService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 场景1：基本事务 - 多个操作要么全部成功，要么全部回滚
     * 如果任何一步抛出异常，所有数据库操作都会回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUserWithRole(User user, Role role) {
        // 步骤1：保存用户
        userService.save(user);
        
        // 步骤2：保存角色
        roleService.save(role);
        
        // 如果这里抛出异常，上面的用户和角色保存都会回滚
        // throw new RuntimeException("模拟异常");
    }
    
    /**
     * 场景2：只读事务 - 用于查询操作，提升性能
     * readOnly=true 告诉数据库这是一个只读事务，可以优化查询性能
     */
    @Transactional(readOnly = true)
    public User getUserWithDetails(Long userId) {
        // 查询用户
        User user = userService.getById(userId);
        
        // 可以进行其他查询操作
        // 这些操作共享同一个只读事务
        
        return user;
    }
    
    /**
     * 场景3：指定不回滚的异常类型
     * 默认情况下，只有RuntimeException和Error会触发回滚
     * 使用noRollbackFor可以指定某些异常不回滚
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = IllegalArgumentException.class)
    public void saveWithExceptionHandling(User user) {
        userService.save(user);
        
        // 这个异常不会导致回滚
        if (user.getName() == null) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        
        // 但这个异常会导致回滚
        // throw new RuntimeException("系统错误");
    }
    
    /**
     * 场景4：事务传播行为 - REQUIRED（默认）
     * 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新事务
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createInRequiredTransaction(User user) {
        userService.save(user);
        // 如果调用此方法时已有事务，则加入该事务
        // 如果没有事务，则创建新事务
    }
    
    /**
     * 场景5：事务传播行为 - REQUIRES_NEW
     * 无论当前是否存在事务，都创建新事务
     * 新事务和原事务互不影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createInNewTransaction(User user) {
        userService.save(user);
        // 这个方法总是在新事务中执行
        // 即使外部方法有事务，也不会影响这个方法
    }
    
    /**
     * 场景6：嵌套事务
     * 如果当前存在事务，则在嵌套事务内执行
     * 嵌套事务可以独立回滚，但不影响外部事务
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void createInNestedTransaction(User user) {
        userService.save(user);
        // 这个方法的回滚不会影响外部事务
        // 但外部事务回滚会影响这个方法
    }
    
    /**
     * 场景7：超时设置
     * 如果事务执行时间超过指定秒数，自动回滚
     */
    @Transactional(timeout = 30, rollbackFor = Exception.class)
    public void saveWithTimeout(User user) {
        // 如果这个方法执行超过30秒，事务会自动回滚
        userService.save(user);
    }
    
    /**
     * 场景8：批量操作事务
     * 批量插入或更新时，确保数据一致性
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateUsers(java.util.List<User> users) {
        for (User user : users) {
            userService.save(user);
            // 如果任何一个用户保存失败，所有用户都会回滚
        }
    }
    
    /**
     * 场景9：手动控制事务回滚
     * 在某些业务逻辑下，可以根据条件手动决定回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveWithManualRollback(User user, boolean shouldRollback) {
        userService.save(user);
        
        // 根据业务逻辑决定是否回滚
        if (shouldRollback) {
            // 手动设置当前事务为回滚状态
            org.springframework.transaction.interceptor.TransactionAspectSupport
                .currentTransactionStatus().setRollbackOnly();
        }
    }
}
