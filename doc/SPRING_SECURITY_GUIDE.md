# Spring Security 集成指南

## 📋 概述

本项目已成功集成 Spring Security，提供了企业级的安全认证和授权功能。

---

## ✅ 已实现的功能

### 1. **用户认证**
- ✅ 基于用户名/密码的表单登录
- ✅ BCrypt 密码加密存储
- ✅ 自定义 UserDetailsService
- ✅ 登录失败处理
- ✅ 会话管理

### 2. **授权控制**
- ✅ 基于角色的访问控制（RBAC）
- ✅ URL 级别的权限控制
- ✅ 动态菜单权限加载
- ✅ 方法级别的安全控制（可扩展）

### 3. **安全防护**
- ✅ CSRF 保护（已禁用，可根据需要启用）
- ✅ 会话固定攻击防护
- ✅ 并发会话控制
- ✅ 安全的登出机制

### 4. **用户体验**
- ✅ 友好的登录页面
- ✅ 错误提示消息
- ✅ 自动跳转到受保护页面
- ✅ 当前用户信息全局可用

---

## 🔧 核心组件

### 1. SecurityConfig.java
**位置**: `src/main/java/com/example/demopet/config/SecurityConfig.java`

主要配置：
- 密码编码器（BCrypt）
- 认证提供者
- 安全过滤链
- 登录/登出配置
- URL 授权规则

### 2. CustomUserDetailsService.java
**位置**: `src/main/java/com/example/demopet/service/CustomUserDetailsService.java`

职责：
- 从数据库加载用户信息
- 验证用户状态
- 加载用户角色和权限
- 返回 Spring Security UserDetails 对象

### 3. GlobalControllerAdvice.java
**位置**: `src/main/java/com/example/demopet/config/GlobalControllerAdvice.java`

新增功能：
- 全局添加当前用户信息到模型
- 从 SecurityContext 获取认证用户
- 所有模板可直接使用 `currentUser` 变量

---

## 🚀 使用方法

### 登录系统

1. **访问登录页面**: http://localhost:8080/login
2. **默认账号**:
   - 用户名: `admin`
   - 密码: `123456`

3. **其他测试账号**:
   - manager / 123456
   - user1 / 123456
   - user2 / 123456
   - user3 / 123456

### 在控制器中获取当前用户

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@GetMapping("/profile")
public String profile() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    // 处理业务逻辑
    return "profile";
}
```

### 在 Thymeleaf 模板中使用

```html
<!-- 显示当前用户名 -->
<span th:text="${currentUser.username}">用户名</span>

<!-- 条件显示（根据登录状态） -->
<div th:if="${currentUser != null}">
    已登录用户可见内容
</div>

<!-- 根据角色显示 -->
<div sec:authorize="hasRole('ADMIN')">
    仅管理员可见
</div>
```

### 在 Service 层进行权限检查

```java
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // 仅管理员可执行
}

@PreAuthorize("hasAuthority('user:create')")
public void createUser() {
    // 需要有 user:create 权限
}
```

---

## 🔐 安全配置详解

### 公开访问的路径
- `/login` - 登录页面
- `/` - 首页
- `/error` - 错误页面
- `/css/**` - 样式文件
- `/js/**` - JavaScript 文件
- `/images/**` - 图片资源

### 需要认证的路径
- `/dashboard` - 仪表板
- `/user/**` - 用户管理
- `/role/**` - 角色管理
- `/menu/**` - 菜单管理
- `/product/**` - 商品管理
- `/order/**` - 订单管理
- 其他所有路径默认需要认证

### 会话配置
- 最大会话数: 1（同一用户只能在一个地方登录）
- 新登录会踢掉旧会话: false（阻止新登录）
- 登出时销毁会话: true
- 删除 JSESSIONID Cookie: true

---

## 📊 密码加密

### BCrypt 加密示例

```java
@Autowired
private PasswordEncoder passwordEncoder;

// 加密密码
String encodedPassword = passwordEncoder.encode("123456");
// 结果类似: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lqkkO9QS3TzCjH3rS

// 验证密码
boolean matches = passwordEncoder.matches("123456", encodedPassword);
```

### 数据库中的密码格式

所有用户密码已使用 BCrypt 加密：
```sql
-- 密码: 123456 的 BCrypt 哈希值
$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lqkkO9QS3TzCjH3rS
```

---

## 🔄 迁移说明

### 从旧系统迁移

如果您之前使用的是自定义登录拦截器，以下是主要变化：

| 旧实现 | 新实现 | 说明 |
|--------|--------|------|
| LoginInterceptor | SecurityFilterChain | 由 Spring Security 统一管理 |
| Session 存储用户 | SecurityContext | 更安全的认证上下文 |
| 手动密码验证 | PasswordEncoder | 标准化的密码加密 |
| 自定义登录逻辑 | formLogin() | 标准的表单登录流程 |

### 代码变更清单

✅ **已删除**:
- `LoginInterceptor.java` - 不再需要（保留但不使用）
- WebConfig 中的拦截器配置
- LoginController 中的手动登录逻辑

✅ **已新增**:
- `SecurityConfig.java` - Spring Security 配置
- `CustomUserDetailsService.java` - 用户认证服务
- GlobalControllerAdvice 中的 currentUser 注入

✅ **已修改**:
- `pom.xml` - 添加 Spring Security 依赖
- `LoginController.java` - 简化为只处理登录页面
- `UserService.java` - 添加密码加密方法
- `data-mysql.sql` - 密码改为 BCrypt 格式
- 所有 `.html` 模板 - 使用 `currentUser` 替代 `session.currentUser`

---

## 🛡️ 安全建议

### 生产环境配置

1. **启用 CSRF 保护**
   ```java
   .csrf(csrf -> csrf.enable())
   ```

2. **配置 HTTPS**
   ```yaml
   server:
     ssl:
       enabled: true
       key-store: classpath:keystore.p12
       key-store-password: your-password
   ```

3. **增强密码策略**
   - 最小长度: 8 字符
   - 必须包含大小写字母、数字、特殊字符
   - 定期更换密码

4. **添加登录尝试限制**
   ```java
   // 使用 Spring Security 的账户锁定功能
   .accountLockedException(new LockedException("账户已锁定"))
   ```

5. **启用审计日志**
   - 记录所有登录/登出事件
   - 记录敏感操作
   - 监控异常行为

### 额外安全措施

- ✅ 实施双因素认证（2FA）
- ✅ 添加 IP 白名单
- ✅ 配置 CORS 策略
- ✅ 实施 API 速率限制
- ✅ 定期安全扫描和渗透测试

---

## 🧪 测试

### 单元测试示例

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
            .param("username", "admin")
            .param("password", "123456"))
            .andExpect(redirectedUrl("/dashboard"));
    }
    
    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
            .param("username", "admin")
            .param("password", "wrong"))
            .andExpect(redirectedUrl("/login?error=true"));
    }
    
    @Test
    void testProtectedResource() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().isFound()); // 重定向到登录
    }
}
```

---

## 📚 参考资料

- [Spring Security 官方文档](https://spring.io/projects/spring-security)
- [BCrypt 密码加密](https://en.wikipedia.org/wiki/Bcrypt)
- [OWASP 安全最佳实践](https://owasp.org/www-project-top-ten/)
- [Thymeleaf + Spring Security](https://www.thymeleaf.org/doc/articles/springsecurity.html)

---

## ❓ 常见问题

### Q1: 编译错误 - DaoAuthenticationProvider 构造函数问题？
A: 在 Spring Security 6.x 中，`DaoAuthenticationProvider` 的构造函数需要传入 `UserDetailsService` 参数。

**正确写法**：
```java
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}
```

**错误写法**（会导致编译错误）：
```java
// ❌ 不要这样写
DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
authProvider.setUserDetailsService(userDetailsService);
```

### Q2: 启动时出现 "Ambiguous mapping" 错误？
A: 这是因为有多个控制器映射到了同一个路径。在集成 Spring Security 后，`LoginController` 和 `IndexController` 都映射了 `/` 路径。

**解决方案**：
- 保留 `IndexController` 中的 `/` 映射（重定向到登录页）
- 删除 `LoginController` 中的 `/` 映射
- Spring Security 会自动处理未认证用户的重定向

**IndexController.java**（保留）：
```java
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}
```

### Q3: 如何自定义登录页面？
A: 修改 `SecurityConfig.java` 中的 `.loginPage("/login")` 配置，并创建对应的 Thymeleaf 模板。

### Q4: 如何实现记住我功能？
A: 在 SecurityConfig 中添加：
```java
.rememberMe(remember -> remember
    .key("uniqueAndSecretKey")
    .tokenValiditySeconds(86400) // 24小时
)
```

### Q5: 如何基于角色显示不同菜单？
A: 在模板中使用：
```html
<div sec:authorize="hasRole('ADMIN')">
    <!-- 管理员菜单 -->
</div>
```

### Q6: 如何处理 AJAX 请求的认证？
A: 配置异常处理器：
```java
.exceptionHandling(ex -> ex
    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
)
```

### Q8: 登录页面出现 "_csrf.parameterName" 错误？
A: 这是因为在 `SecurityConfig` 中禁用了 CSRF 保护，但模板中仍然引用了 `_csrf` 变量。

**解决方案**：
从 `login.html` 中删除 CSRF token 相关代码：

```html
<!-- ❌ 删除这行（CSRF 已禁用） -->
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
```

**如果需要启用 CSRF**：
在 `SecurityConfig.java` 中修改：
```java
.csrf(csrf -> csrf.enable())  // 启用 CSRF
```

然后在所有 POST 表单中添加 CSRF token。

### Q9: 密码忘记了怎么办？
A: 实现密码重置功能，通过邮箱发送重置链接，或使用管理员后台重置密码。

---

## 📞 技术支持

如有问题，请：
1. 查看 Spring Security 官方文档
2. 检查项目 Issue 列表
3. 联系开发团队

---

**集成完成时间**: 2026-04-30  
**Spring Security 版本**: 6.x (Spring Boot 4.0.6)  
**最后更新**: 2026-04-30
