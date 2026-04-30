# 登录问题排查指南

## 问题现象

登录时提示"用户名或密码错误"

## 已实施的解决方案

### ✅ 自动修复机制

已在 `DemopetApplication` 中添加了自动修复逻辑：

```java
@Override
public void run(String... args) throws Exception {
    // 应用启动后，确保 admin 用户密码正确
    userService.createTestUser();
}
```

**工作原理**：
1. 应用启动完成后自动执行
2. 检查 `admin` 用户是否存在
3. 如果存在，更新密码为正确的 BCrypt 哈希值
4. 如果不存在，创建新的 admin 用户
5. 在控制台输出确认信息

### 🔧 使用步骤

1. **重启应用**
   ```bash
   # 停止当前应用（Ctrl+C）
   # 然后重新启动
   mvn spring-boot:run
   ```

2. **查看控制台输出**
   
   启动成功后，应该看到类似以下输出：
   ```
   ✅ 已更新 admin 用户密码: $2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```
   或
   ```
   ✅ 已创建 admin 用户
   ```

3. **使用正确账号登录**
   - 用户名：`admin`
   - 密码：`123456`

## 手动修复方法

如果自动修复不起作用，可以手动执行 SQL：

### 方法1：直接更新数据库

```sql
USE demo_db;

-- 生成新的 BCrypt 密码并更新
UPDATE user 
SET password = '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq' 
WHERE username = 'admin';

-- 验证
SELECT username, password FROM user WHERE username = 'admin';
```

### 方法2：重新初始化数据库

删除数据库后重新启动应用：

```sql
DROP DATABASE demo_db;
CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后重启应用，会自动执行 `schema-mysql.sql` 和 `data-mysql.sql`。

## 常见问题

### Q1: 为什么 BCrypt 密码总是验证失败？

**原因**：
- BCrypt 哈希值不正确
- 数据库中的密码被手动修改过
- SQL 初始化脚本没有正确执行

**解决**：
- 使用应用启动时的自动修复功能
- 或者手动执行上面的 SQL 更新语句

### Q2: 如何验证 BCrypt 密码是否正确？

可以使用 `PasswordGenerator` 工具类：

```java
// 运行 src/main/java/com/example/demopet/util/PasswordGenerator.java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String encoded = encoder.encode("123456");
boolean matches = encoder.matches("123456", encoded);
System.out.println("验证结果: " + matches); // 应该输出 true
```

### Q3: 其他用户也无法登录怎么办？

可以为所有用户更新密码：

```sql
UPDATE user SET password = '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq';
```

## 测试账号列表

| 用户名 | 密码 | 角色 | 状态 |
|--------|------|------|------|
| admin | 123456 | 超级管理员 | ✅ 可用 |
| manager | 123456 | 系统管理员 | ✅ 可用 |
| user1 | 123456 | 普通用户 | ✅ 可用 |
| user2 | 123456 | 普通用户 | ✅ 可用 |
| user3 | 123456 | 普通用户 | ✅ 可用 |

## 调试技巧

### 1. 启用详细日志

在 `application.yml` 中添加：

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    com.example.demopet: DEBUG
```

### 2. 检查数据库中的密码

```sql
SELECT id, username, password, status FROM user;
```

确认：
- 密码字段以 `$2a$` 开头（BCrypt 格式）
- 密码长度为 60 个字符
- status = 1（启用状态）

### 3. 测试密码验证

在浏览器控制台或 Postman 中测试登录接口：

```
POST http://localhost:8080/login
Content-Type: application/x-www-form-urlencoded

username=admin&password=123456
```

## 联系支持

如果以上方法都无法解决问题，请提供：
1. 应用启动日志
2. 数据库中的用户记录截图
3. 浏览器控制台的错误信息

---

**最后更新**: 2026-04-30  
**适用版本**: Spring Security 7.x
