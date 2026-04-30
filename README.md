# 电商后台管理系统 (demopet)

## 📋 项目概述

这是一个基于 Spring Boot 构建的电商后台管理系统，提供完整的用户权限管理、商品管理、订单管理等功能。系统采用 RBAC（基于角色的访问控制）模型，支持动态菜单配置和多角色权限管理。

### ✨ 核心特性

- ✅ **完整的用户权限管理体系** - 基于 RBAC 模型的权限控制
- ✅ **Spring Security 企业级安全认证** - BCrypt 密码加密、CSRF 保护
- ✅ **动态菜单加载和配置** - 根据用户角色动态显示菜单
- ✅ **商品和订单管理功能** - 完整的电商业务流程
- ✅ **响应式后台界面设计** - 适配各种屏幕尺寸
- ✅ **数据库自动初始化** - 启动时自动创建表和初始数据
- ✅ **事务管理** - 声明式事务保证数据一致性

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
│   │   ├── SecurityConfig.java            # Spring Security 配置
│   │   └── WebConfig.java                 # Web配置（拦截器）
│   ├── controller/                # 控制器层
│   │   ├── DashboardController.java       # 仪表板
│   │   ├── IndexController.java           # 首页
│   │   ├── LoginController.java           # 登录认证
│   │   ├── MenuController.java            # 菜单管理
│   │   ├── OrderController.java           # 订单管理
│   │   ├── ProductController.java         # 商品管理
│   │   ├── RoleController.java            # 角色管理
│   │   └── UserController.java            # 用户管理
│   ├── entity/                    # 实体类
│   │   ├── Menu.java                      # 菜单实体
│   │   ├── Order.java                     # 订单实体
│   │   ├── OrderItem.java                 # 订单项实体
│   │   ├── Product.java                   # 商品实体
│   │   ├── Role.java                      # 角色实体
│   │   ├── RoleMenu.java                  # 角色菜单关联
│   │   ├── User.java                      # 用户实体
│   │   └── UserRole.java                  # 用户角色关联
│   ├── interceptor/               # 拦截器
│   │   └── LoginInterceptor.java          # 登录验证拦截器
│   ├── mapper/                    # MyBatis Mapper接口
│   │   ├── MenuMapper.java
│   │   ├── OrderItemMapper.java
│   │   ├── OrderMapper.java
│   │   ├── ProductMapper.java
│   │   ├── RoleMapper.java
│   │   ├── RoleMenuMapper.java
│   │   ├── UserMapper.java
│   │   └── UserRoleMapper.java
│   ├── service/                   # 业务逻辑层
│   │   ├── CustomUserDetailsService.java  # 自定义用户详情服务
│   │   ├── MenuService.java
│   │   ├── OrderService.java
│   │   ├── ProductService.java
│   │   ├── RoleMenuService.java
│   │   ├── RoleService.java
│   │   ├── TransactionExampleService.java
│   │   ├── UserRoleService.java
│   │   └── UserService.java
│   ├── util/                      # 工具类
│   │   └── PasswordGenerator.java         # 密码生成器
│   └── DemopetApplication.java    # 启动类
├── src/main/resources/
│   ├── db/                        # 数据库脚本
│   │   ├── migration/
│   │   ├── data-mysql.sql               # 初始化数据
│   │   └── schema-mysql.sql             # 表结构定义
│   ├── static/                    # 静态资源
│   ├── templates/                 # Thymeleaf模板
│   │   ├── menu/                        # 菜单管理页面
│   │   ├── order/                       # 订单管理页面
│   │   ├── product/                     # 商品管理页面
│   │   ├── role/                        # 角色管理页面
│   │   ├── user/                        # 用户管理页面
│   │   ├── dashboard.html               # 仪表板页面
│   │   ├── index.html                   # 首页
│   │   └── login.html                   # 登录页面
│   └── application.yml            # 应用配置文件
├── src/test/                      # 测试代码
├── doc/                           # 项目文档
├── target/                        # 编译输出
├── pom.xml                        # Maven配置文件
└── README.md                      # 项目说明
```

---

## 🚀 快速开始

### 环境要求

- JDK 17 或更高版本
- MySQL 8.0 或更高版本
- Maven 3.6 或更高版本

### 安装步骤

#### 1. 克隆项目

```bash
git clone <repository-url>
cd demopet
```

#### 2. 配置数据库

创建数据库：

```sql
CREATE DATABASE demo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password  # 修改为您的密码
```

#### 3. 构建项目

```bash
mvn clean package
```

#### 4. 运行应用

```bash
# 方式1：使用 Maven
mvn spring-boot:run

# 方式2：直接运行 JAR
java -jar target/demopet-0.0.1-SNAPSHOT.jar
```

#### 5. 访问系统

- 浏览器访问：http://localhost:8080
- 默认账号：`admin` / `123456`

---

## 🗄️ 数据库设计

### 核心表结构

#### 1. 用户权限相关表

**user - 用户信息表**
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    remark VARCHAR(500) COMMENT '备注'
);
```

**role - 角色信息表**
```sql
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用'
);
```

**menu - 菜单权限表**
```sql
CREATE TABLE menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_type CHAR(1) NOT NULL COMMENT '菜单类型：M-目录 C-菜单 F-按钮',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(200) COMMENT '组件路径',
    permission VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    visible TINYINT DEFAULT 1 COMMENT '是否可见',
    status TINYINT DEFAULT 1 COMMENT '状态'
);
```

**user_role - 用户角色关联表**
```sql
CREATE TABLE user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE KEY uk_user_role (user_id, role_id)
);
```

**role_menu - 角色菜单关联表**
```sql
CREATE TABLE role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    UNIQUE KEY uk_role_menu (role_id, menu_id)
);
```

#### 2. 业务数据表

**product - 商品信息表**
```sql
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    product_code VARCHAR(50) UNIQUE COMMENT '商品编码',
    category_id BIGINT COMMENT '分类ID',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    image_url VARCHAR(500) COMMENT '图片URL',
    description TEXT COMMENT '商品描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-上架，0-下架'
);
```

**orders - 订单主表**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总额',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
    receiver_name VARCHAR(50) COMMENT '收货人',
    receiver_phone VARCHAR(20) COMMENT '收货电话',
    receiver_address VARCHAR(500) COMMENT '收货地址',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    complete_time DATETIME COMMENT '完成时间'
);
```

**order_item - 订单明细表**
```sql
CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计'
);
```

### 关系图

```
用户权限关系：
User ──(N:M)── Role ──(N:M)── Menu

业务数据关系：
Product_Category ──(1:N)── Product
Orders ──(1:N)── Order_Item
User ──(1:N)── Orders
```

---

## 🔐 权限系统

### RBAC 模型

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

### Spring Security 集成

**核心组件：**
- **SecurityConfig** - 安全配置类
- **CustomUserDetailsService** - 自定义用户详情服务
- **PasswordEncoder** - BCrypt 密码编码器

**安全特性：**
- ✅ BCrypt 密码加密
- ✅ CSRF 保护
- ✅ 会话管理
- ✅ URL 级别的访问控制
- ✅ 基于角色的权限管理

---

## 🎯 主要功能模块

### 1. 用户管理

- 用户信息的增删改查
- 用户名唯一性验证
- 用户状态管理（启用/禁用）
- 角色分配和管理
- 分页查询和搜索

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
server:
  port: 8080

spring:
  application:
    name: demopet
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password
  
  sql:
    init:
      schema-locations: classpath:db/schema-mysql.sql
      data-locations: classpath:db/data-mysql.sql
      mode: always
  
  thymeleaf:
    cache: false  # 开发环境关闭缓存

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
```

### 安全配置

受保护的路径：
- `/dashboard` - 仪表板
- `/user/**` - 用户管理
- `/role/**` - 角色管理
- `/menu/**` - 菜单管理
- `/product/**` - 商品管理
- `/order/**` - 订单管理

公开访问路径：
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

### 事务管理

使用 `@Transactional` 注解进行声明式事务管理：

```java
@Service
public class OrderService {
    
    @Transactional
    public void createOrder(Order order) {
        // 订单创建逻辑
        // 自动回滚如果发生异常
    }
}
```

---

## 📊 性能优化建议

### 数据库优化

- 为常用查询字段添加索引
- 优化复杂查询语句
- 定期清理无用数据
- 使用连接池（HikariCP）

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
- ✅ CSRF 保护机制
- ✅ 会话管理和并发控制
- ✅ URL 级别的权限验证
- ✅ 安全的登出流程
- ✅ 用户状态验证

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

### Docker 部署

创建 `Dockerfile`：

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/demopet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

构建和运行：

```bash
docker build -t demopet .
docker run -p 8080:8080 demopet
```

### 生产环境配置

- 使用外部配置文件
- 配置HTTPS证书
- 设置合理的JVM参数
- 配置日志轮转和监控

---

## 📈 监控和维护

### 健康检查

Spring Boot Actuator 端点：
- `/actuator/health` - 健康状态
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 性能指标

### 日志管理

- 结构化日志输出
- 错误追踪和告警
- 审计日志记录

---

## 📝 Maven Site 文档

项目配置了完整的 Maven Site 生成功能，可以生成详细的项目文档。

### 生成站点

```bash
mvn clean site -DskipTests
```

### 查看站点

生成的站点位于 `target/site/` 目录，打开 `index.html` 即可查看。

### 包含的报告

- 项目概要
- 依赖信息
- 插件信息
- 团队信息
- SCM 信息
- 源码交叉引用
- 测试报告
- 代码覆盖率报告

---

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证

---

## 📞 支持和联系

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件至：support@example.com
- 查看项目 Wiki 文档

---

## 📚 相关文档

- [Spring Security 指南](../../../doc/SPRING_SECURITY_GUIDE.md)
- [事务管理指南](../../../doc/TRANSACTION_GUIDE.md)
- [登录问题排查](../../../doc/LOGIN_TROUBLESHOOTING.md)
- [快速开始](../../../doc/QUICK_START_SECURITY.md)
- [项目文档](../../../doc/PROJECT_DOCUMENTATION.md)

---

**最后更新**: 2026-04-30  
**版本**: v1.0.0
