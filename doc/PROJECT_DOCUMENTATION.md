# 电商后台管理系统 (demopet)

## 📋 项目概述

这是一个基于 Spring Boot 构建的电商后台管理系统，提供完整的用户权限管理、商品管理、订单管理等功能。系统采用 RBAC（基于角色的访问控制）模型，支持动态菜单配置和多角色权限管理。

### 核心特性
- ✅ 完整的用户权限管理体系
- ✅ **Spring Security 企业级安全认证**
- ✅ BCrypt 密码加密存储
- ✅ 动态菜单加载和配置
- ✅ 商品和订单管理功能
- ✅ 响应式后台界面设计
- ✅ 数据库自动初始化
- ✅ CSRF 保护和会话管理

---

## 🛠️ 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 4.0.6 |
| ORM框架 | MyBatis-Plus | 3.5.15 |
| 安全框架 | Spring Security | 6.x |
| 模板引擎 | Thymeleaf | - |
| 数据库 | MySQL | 8.0+ |
| Java版本 | JDK | 17 |
| 构建工具 | Maven | 3.6+ |
| 其他依赖 | Lombok, Actuator | - |

---

## 📁 项目结构

```
demopet/
├── src/main/java/com/example/demopet/
│   ├── config/                    # 配置类
│   │   ├── GlobalControllerAdvice.java    # 全局控制器建议（动态菜单）
│   │   └── WebConfig.java                 # Web配置（拦截器）
│   ├── controller/                # 控制器层
│   │   ├── DashboardController.java       # 仪表板
│   │   ├── LoginController.java           # 登录认证
│   │   ├── MenuController.java            # 菜单管理
│   │   ├── OrderController.java           # 订单管理
│   │   ├── ProductController.java         # 商品管理
│   │   ├── RoleController.java            # 角色管理
│   │   └── UserController.java            # 用户管理
│   ├── entity/                    # 实体类
│   │   ├── User.java                      # 用户实体
│   │   ├── Role.java                      # 角色实体
│   │   ├── Menu.java                      # 菜单实体
│   │   ├── UserRole.java                  # 用户角色关联
│   │   ├── RoleMenu.java                  # 角色菜单关联
│   │   ├── Product.java                   # 商品实体
│   │   ├── Order.java                     # 订单实体
│   │   └── OrderItem.java                 # 订单项实体
│   ├── interceptor/               # 拦截器
│   │   └── LoginInterceptor.java          # 登录验证拦截器
│   ├── mapper/                    # MyBatis Mapper接口
│   ├── service/                   # 业务逻辑层
│   │   ├── UserService.java               # 用户服务
│   │   ├── RoleService.java               # 角色服务
│   │   ├── MenuService.java               # 菜单服务
│   │   ├── ProductService.java            # 商品服务
│   │   ├── OrderService.java              # 订单服务
│   │   └── ...                            # 其他服务
│   └── DemopetApplication.java    # 启动类
├── src/main/resources/
│   ├── db/                        # 数据库脚本
│   │   ├── schema-mysql.sql             # 表结构定义
│   │   └── data-mysql.sql               # 初始化数据
│   ├── templates/                 # Thymeleaf模板
│   │   ├── dashboard.html               # 仪表板页面
│   │   ├── login.html                   # 登录页面
│   │   ├── user/                        # 用户管理页面
│   │   ├── role/                        # 角色管理页面
│   │   ├── menu/                        # 菜单管理页面
│   │   ├── product/                     # 商品管理页面
│   │   └── order/                       # 订单管理页面
│   └── application.yml            # 应用配置文件
└── pom.xml                        # Maven配置文件
```

---

## 🗄️ 数据库设计

### 核心表结构

#### 1. 用户权限相关表
- **user** - 用户信息表
- **role** - 角色信息表
- **menu** - 菜单权限表
- **user_role** - 用户角色关联表
- **role_menu** - 角色菜单关联表

#### 2. 业务数据表
- **product_category** - 商品分类表
- **product** - 商品信息表
- **orders** - 订单主表
- **order_item** - 订单明细表

### 关系图
```
User ──(N:M)── Role ──(N:M)── Menu
Product_Category ──(1:N)── Product
Orders ──(1:N)── Order_Item
```

---

## 🚀 快速开始

### 环境要求
- JDK 17 或更高版本
- MySQL 8.0 或更高版本
- Maven 3.6 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd demopet
   ```

2. **配置数据库**
   - 创建数据库：`CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
   - 修改 `src/main/resources/application.yml` 中的数据库连接信息

3. **构建项目**
   ```bash
   mvn clean package
   ```

4. **运行应用**
   ```bash
   mvn spring-boot:run
   # 或者
   java -jar target/demopet-0.0.1-SNAPSHOT.jar
   ```

5. **访问系统**
   - 浏览器访问：http://localhost:8080
   - 默认账号：admin / 123456

### 自动初始化
系统启动时会自动执行以下操作：
- 创建所有必需的数据库表
- 插入初始化管理员账户和基础数据
- 配置默认的角色和权限关系

---

## 🔐 权限系统

### Spring Security 集成

本项目已集成 **Spring Security**，提供企业级的安全认证和授权功能。

#### 核心组件
- **SecurityConfig**: 安全配置类，定义认证和授权规则
- **CustomUserDetailsService**: 自定义用户详情服务，从数据库加载用户信息
- **PasswordEncoder**: BCrypt 密码编码器，确保密码安全存储
- **GlobalControllerAdvice**: 全局注入当前用户信息到所有模板

#### 安全特性
- ✅ BCrypt 密码加密
- ✅ CSRF 保护（可配置）
- ✅ 会话管理和并发控制
- ✅ URL 级别的访问控制
- ✅ 基于角色的权限管理

详细文档请参考：[SPRING_SECURITY_GUIDE.md](SPRING_SECURITY_GUIDE.md)

### RBAC模型
系统采用标准的 RBAC（Role-Based Access Control）权限模型：

```
用户(User) → 角色(Role) → 权限(Menu)
```

### 默认角色
| 角色名称 | 角色编码 | 权限描述 |
|---------|---------|---------|
| 超级管理员 | SUPER_ADMIN | 拥有所有权限 |
| 系统管理员 | ADMIN | 系统管理权限 |
| 商品管理员 | PRODUCT_ADMIN | 商品管理权限 |
| 订单管理员 | ORDER_ADMIN | 订单管理权限 |
| 普通用户 | USER | 基本使用权限 |

### 动态菜单
- 菜单从数据库动态加载
- 根据用户角色显示对应菜单项
- 支持多级菜单结构
- 可实时配置和调整

---

## 🎯 主要功能模块

### 1. 用户管理
- 用户信息的增删改查
- 用户名唯一性验证
- 用户状态管理（启用/禁用）
- 角色分配和管理

### 2. 角色管理
- 角色的创建和维护
- 权限配置和分配
- 角色状态管理
- 批量权限设置

### 3. 菜单管理
- 菜单项的层级管理
- 菜单权限标识配置
- 图标和排序设置
- 动态菜单预览

### 4. 商品管理
- 商品信息的完整管理
- 商品分类维护
- 价格和库存管理
- 商品上下架控制

### 5. 订单管理
- 订单生命周期管理
- 订单状态跟踪
- 支付和发货处理
- 订单统计分析

### 6. 数据统计
- 仪表板数据展示
- 关键指标统计
- 最近活动记录
- 快捷操作入口

---

## ⚙️ 配置说明

### 应用配置 (application.yml)
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_db
    username: root
    password: your_password
  sql:
    init:
      schema-locations: classpath:db/schema-mysql.sql
      data-locations: classpath:db/data-mysql.sql
      mode: always
  thymeleaf:
    cache: false  # 开发环境关闭缓存
```

### 拦截器配置
系统使用 Spring Security 进行认证和授权，保护以下路径：
- `/dashboard` - 仪表板
- `/user/**` - 用户管理
- `/role/**` - 角色管理  
- `/menu/**` - 菜单管理
- `/product/**` - 商品管理
- `/order/**` - 订单管理

公开访问路径（无需登录）：
- `/login` - 登录页面
- `/` - 首页
- `/error` - 错误页面
- `/css/**`, `/js/**`, `/images/**` - 静态资源

---

## 🔧 开发指南

### 添加新功能模块

1. **创建实体类** - 在 `entity` 包中定义数据模型
2. **创建Mapper接口** - 继承 `BaseMapper<T>` 
3. **创建Service类** - 继承 `ServiceImpl<Mapper, Entity>`
4. **创建Controller** - 处理HTTP请求和业务逻辑
5. **创建前端页面** - 在 `templates` 中添加Thymeleaf模板
6. **配置菜单权限** - 在数据库中添加对应的菜单项

### 代码规范
- 使用 Lombok 简化实体类代码
- 遵循 RESTful API 设计规范
- 统一异常处理和返回格式
- 添加适当的注释和文档

---

## 📊 性能优化建议

### 数据库优化
- 为常用查询字段添加索引
- 优化复杂查询语句
- 定期清理无用数据

### 缓存策略
- 菜单数据可以缓存减少数据库查询
- 用户会话信息可以考虑Redis存储
- 静态资源使用CDN加速

### 前端优化
- 压缩CSS和JavaScript文件
- 图片资源优化和懒加载
- 减少不必要的DOM操作

---

## 🔒 安全考虑

### Spring Security 安全措施
- ✅ BCrypt 密码加密存储
- ✅ 基于角色的访问控制（RBAC）
- ✅ CSRF 保护机制（可配置）
- ✅ 会话管理和并发控制
- ✅ URL 级别的权限验证
- ✅ 安全的登出流程
- ✅ 用户状态验证（禁用账户无法登录）

### 建议增强
- 启用 HTTPS/TLS 加密传输
- 实施双因素认证（2FA）
- 添加登录尝试次数限制
- 实施 IP 白名单
- 定期安全审计和渗透测试
- 添加操作审计日志

---

## 🧪 测试

### 运行测试
```bash
mvn test
```

### 测试覆盖
- 单元测试：Service层业务逻辑
- 集成测试：Controller API接口
- 端到端测试：完整业务流程

---

## 🚢 部署

### Docker部署（推荐）
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/demopet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### 生产环境配置
- 使用外部配置文件
- 配置HTTPS证书
- 设置合理的JVM参数
- 配置日志轮转和监控

---

## 📈 监控和维护

### 健康检查
- Spring Boot Actuator 端点
- 数据库连接池监控
- 系统资源使用情况

### 日志管理
- 结构化日志输出
- 错误追踪和告警
- 审计日志记录

---

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---

## 📞 支持和联系

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件至：support@example.com
- 查看项目 Wiki 文档

---

**最后更新**: 2026-04-30  
**版本**: v1.0.0