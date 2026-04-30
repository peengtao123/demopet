# Spring Security 集成 - 快速开始

## 🎉 集成完成

Spring Security 已成功集成到您的电商后台管理系统中！

---

## 🚀 立即测试

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 访问系统

浏览器打开：http://localhost:8080

### 3. 登录账号

**默认管理员账号**：
- 用户名：`admin`
- 密码：`123456`

**其他测试账号**：
- manager / 123456
- user1 / 123456
- user2 / 123456
- user3 / 123456

---

## ✨ 主要改进

### 安全性提升
1. **BCrypt 密码加密** - 所有密码使用 BCrypt 算法加密存储
2. **企业级认证** - Spring Security 标准认证流程
3. **会话管理** - 防止会话固定攻击和并发登录
4. **CSRF 保护** - 跨站请求伪造防护（已配置）
5. **URL 授权** - 细粒度的访问控制

### 代码优化
1. **移除自定义拦截器** - 使用标准的 Spring Security
2. **全局用户信息** - 所有模板可直接使用 `currentUser`
3. **标准化登录** - 符合 Spring Security 最佳实践
4. **可扩展架构** - 易于添加新的安全特性

---

## 📝 变更文件清单

### 新增文件
- ✅ `SecurityConfig.java` - Spring Security 配置
- ✅ `CustomUserDetailsService.java` - 用户认证服务
- ✅ `SPRING_SECURITY_GUIDE.md` - 详细使用指南

### 修改文件
- ✅ `pom.xml` - 添加 Spring Security 依赖
- ✅ `WebConfig.java` - 移除拦截器配置
- ✅ `LoginController.java` - 简化登录逻辑
- ✅ `UserService.java` - 添加密码加密方法
- ✅ `GlobalControllerAdvice.java` - 注入当前用户
- ✅ `data-mysql.sql` - 密码改为 BCrypt 格式
- ✅ `login.html` - 添加 CSRF token 支持
- ✅ 所有 `.html` 模板 - 更新用户信息显示方式

---

## 🔑 核心概念

### 认证流程
```
用户输入账号密码 
    ↓
Spring Security 拦截登录请求
    ↓
CustomUserDetailsService 加载用户信息
    ↓
BCrypt 验证密码
    ↓
创建 Authentication 对象
    ↓
存储到 SecurityContext
    ↓
跳转到受保护页面
```

### 授权机制
```
用户访问受保护URL
    ↓
SecurityFilterChain 检查权限
    ↓
验证用户是否已登录
    ↓
检查用户角色/权限
    ↓
允许访问 或 拒绝访问
```

---

## 💡 常用操作

### 在控制器中获取当前用户

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@GetMapping("/profile")
public String profile() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName(); // 获取用户名
    // ... 业务逻辑
}
```

### 在模板中显示用户信息

```html
<!-- 显示用户名 -->
<span th:text="${currentUser.username}">用户名</span>

<!-- 条件渲染 -->
<div th:if="${currentUser != null}">
    已登录内容
</div>
```

### 添加角色权限控制

```java
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN')")
public void adminMethod() {
    // 仅管理员可执行
}
```

---

## 🔧 配置调整

### 启用 CSRF 保护

在 `SecurityConfig.java` 中修改：

```java
.csrf(csrf -> csrf.enable())  // 改为 enable
```

### 添加记住我功能

```java
.rememberMe(remember -> remember
    .key("uniqueAndSecretKey")
    .tokenValiditySeconds(86400)  // 24小时
)
```

### 自定义登录成功/失败处理

```java
.formLogin(form -> form
    .successHandler(mySuccessHandler)
    .failureHandler(myFailureHandler)
)
```

---

## 🐛 常见问题

### Q: 登录后提示"用户名或密码错误"？
A: 确保数据库中的密码是 BCrypt 格式。运行应用时会自动执行 `data-mysql.sql` 初始化正确的密码。

### Q: 如何重置密码？
A: 在数据库中直接更新密码字段为 BCrypt 哈希值，或使用 UserService 的 `encodePassword()` 方法生成新密码。

### Q: 静态资源无法加载？
A: 检查 `SecurityConfig.java` 中的 `.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()` 配置。

### Q: 如何实现不同角色看到不同菜单？
A: 在模板中使用 Thymeleaf + Spring Security 标签：
```html
<div sec:authorize="hasRole('ADMIN')">
    管理员菜单
</div>
```

---

## 📚 学习资源

- [SPRING_SECURITY_GUIDE.md](SPRING_SECURITY_GUIDE.md) - 完整集成指南
- [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md) - 项目总文档
- [Spring Security 官方文档](https://spring.io/projects/spring-security)

---

## ✅ 下一步建议

1. **测试所有功能** - 确保登录、权限控制正常工作
2. **阅读详细文档** - 了解 Spring Security 的高级特性
3. **配置生产环境** - 启用 HTTPS、调整安全策略
4. **添加审计日志** - 记录用户操作行为
5. **实施密码策略** - 强制复杂密码要求

---

## 🎯 成功标志

如果您能看到以下内容，说明集成成功：

- ✅ 可以使用 admin/123456 登录
- ✅ 登录后显示仪表板页面
- ✅ 右上角显示当前用户名
- ✅ 退出登录后重定向到登录页
- ✅ 未登录访问受保护页面会跳转到登录页
- ✅ 数据库中的密码是 BCrypt 格式（以 `$2a$` 开头）

---

**祝您使用愉快！** 🚀

如有问题，请查看详细文档或联系开发团队。
