-- =============================================
-- 电商后台管理系统 - 初始化数据
-- =============================================

-- 1. 初始化用户数据 (密码: 123456, BCrypt加密)
-- 注意: BCrypt 每次生成的哈希都不同，但都能验证 '123456'
DELETE FROM `user`;
INSERT INTO `user` (id, username, password, name, email, phone, status) VALUES
(1, 'admin', '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq', '超级管理员', 'admin@example.com', '13800138000', 1),
(2, 'manager', '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq', '管理员', 'manager@example.com', '13800138001', 1),
(3, 'user1', '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq', '张三', 'zhangsan@example.com', '13800138002', 1),
(4, 'user2', '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq', '李四', 'lisi@example.com', '13800138003', 1),
(5, 'user3', '$2a$10$rOCPYOzCQLEPzKvZrOCPYOuJGZ9VqZ9VqZ9VqZ9VqZ9VqZ9VqZ9Vq', '王五', 'wangwu@example.com', '13800138004', 1);

-- 2. 初始化角色数据
DELETE FROM `role`;
INSERT INTO `role` (id, role_name, role_code, description, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1),
(2, '系统管理员', 'ADMIN', '系统管理权限', 1),
(3, '商品管理员', 'PRODUCT_ADMIN', '商品管理权限', 1),
(4, '订单管理员', 'ORDER_ADMIN', '订单管理权限', 1),
(5, '普通用户', 'USER', '基本使用权限', 1);

-- 3. 初始化菜单数据
DELETE FROM `menu`;
INSERT INTO `menu` (id, menu_name, menu_code, parent_id, path, component, sort, icon, menu_type, permission, status) VALUES
-- 一级菜单（目录）
(1, '系统管理', 'SYSTEM', 0, NULL, NULL, 1, '⚙️', 1, NULL, 1),
(2, '商品管理', 'PRODUCT', 0, NULL, NULL, 2, '📦', 1, NULL, 1),
(3, '订单管理', 'ORDER', 0, NULL, NULL, 3, '🛒', 1, NULL, 1),
(4, '数据统计', 'STATISTICS', 0, NULL, NULL, 4, '📊', 1, NULL, 1),

-- 系统管理子菜单
(10, '用户管理', 'USER_MANAGE', 1, '/user', 'admin/user/index', 1, '👥', 2, 'system:user:list', 1),
(11, '角色管理', 'ROLE_MANAGE', 1, '/role', 'admin/role/index', 2, '🔐', 2, 'system:role:list', 1),
(12, '菜单管理', 'MENU_MANAGE', 1, '/menu', 'admin/menu/index', 3, '📋', 2, 'system:menu:list', 1),

-- 商品管理子菜单
(20, '商品列表', 'PRODUCT_LIST', 2, '/product', 'admin/product/index', 1, '📦', 2, 'product:list', 1),

-- 订单管理子菜单
(30, '订单列表', 'ORDER_LIST', 3, '/order', 'admin/order/index', 1, '📝', 2, 'order:list', 1),

-- 数据统计子菜单
(40, '仪表板', 'DASHBOARD', 4, '/dashboard', 'admin/dashboard', 1, '📊', 2, 'statistics:dashboard', 1);

-- 4. 初始化用户角色关联
DELETE FROM `user_role`;
INSERT INTO `user_role` (user_id, role_id) VALUES
(1, 1), -- admin -> SUPER_ADMIN
(2, 2), -- manager -> ADMIN
(3, 5), -- user1 -> USER
(4, 5), -- user2 -> USER
(5, 5); -- user3 -> USER

-- 5. 初始化角色菜单关联 (超级管理员拥有所有权限)
DELETE FROM `role_menu`;
INSERT INTO `role_menu` (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), -- 超级管理员所有一级菜单
(1, 10), (1, 11), (1, 12), -- 系统管理子菜单
(1, 20), -- 商品管理子菜单
(1, 30), -- 订单管理子菜单
(1, 40), -- 数据统计子菜单

-- 系统管理员权限
(2, 1), (2, 4), -- 系统管理和数据统计
(2, 10), (2, 11), (2, 12), (2, 40),

-- 商品管理员权限
(3, 2), (3, 20),

-- 订单管理员权限
(4, 3), (4, 30);

-- 6. 初始化商品分类数据
DELETE FROM `product_category`;
INSERT INTO `product_category` (id, category_name, parent_id, sort, status) VALUES
(1, '电子产品', 0, 1, 1),
(2, '服装服饰', 0, 2, 1),
(3, '食品饮料', 0, 3, 1),
(4, '家居用品', 0, 4, 1),
(10, '手机', 1, 1, 1),
(11, '电脑', 1, 2, 1),
(12, '平板', 1, 3, 1),
(20, '男装', 2, 1, 1),
(21, '女装', 2, 2, 1),
(22, '童装', 2, 3, 1);

-- 7. 初始化商品数据
DELETE FROM `product`;
INSERT INTO `product` (id, product_name, product_code, category_id, price, original_price, stock, sales, description, main_image, status) VALUES
(1, 'iPhone 15 Pro', 'IPHONE15PRO', 10, 8999.00, 9999.00, 100, 50, 'Apple iPhone 15 Pro 256GB', 'https://via.placeholder.com/300x300?text=iPhone+15+Pro', 1),
(2, 'MacBook Pro 14', 'MACBOOK14', 11, 14999.00, 16999.00, 50, 30, 'Apple MacBook Pro 14英寸 M3芯片', 'https://via.placeholder.com/300x300?text=MacBook+Pro', 1),
(3, 'iPad Air', 'IPADAIR', 12, 4799.00, 5299.00, 80, 40, 'Apple iPad Air 11英寸', 'https://via.placeholder.com/300x300?text=iPad+Air', 1),
(4, '男士T恤', 'MEN_TSHIRT', 20, 99.00, 199.00, 500, 200, '纯棉男士短袖T恤', 'https://via.placeholder.com/300x300?text=T-Shirt', 1),
(5, '女士连衣裙', 'WOMEN_DRESS', 21, 299.00, 499.00, 300, 150, '夏季女士连衣裙', 'https://via.placeholder.com/300x300?text=Dress', 1),
(6, '儿童套装', 'KIDS_SET', 22, 159.00, 259.00, 400, 100, '儿童春夏套装', 'https://via.placeholder.com/300x300?text=Kids+Set', 1),
(7, '有机牛奶', 'ORGANIC_MILK', 3, 68.00, 88.00, 1000, 500, '有机纯牛奶 1L*12盒', 'https://via.placeholder.com/300x300?text=Milk', 1),
(8, '进口咖啡', 'IMPORT_COFFEE', 3, 128.00, 168.00, 600, 300, '进口咖啡豆 500g', 'https://via.placeholder.com/300x300?text=Coffee', 1),
(9, '智能台灯', 'SMART_LAMP', 4, 199.00, 299.00, 200, 80, 'LED智能护眼台灯', 'https://via.placeholder.com/300x300?text=Lamp', 1),
(10, '四件套', 'BEDDING_SET', 4, 399.00, 599.00, 150, 60, '纯棉床上四件套', 'https://via.placeholder.com/300x300?text=Bedding', 1);

-- 8. 初始化订单数据
DELETE FROM `orders`;
INSERT INTO `orders` (id, order_no, user_id, total_amount, pay_amount, freight_amount, status, payment_type, receiver_name, receiver_phone, receiver_address) VALUES
(1, 'ORD20260430001', 3, 9098.00, 9098.00, 0.00, 3, 1, '张三', '13800138002', '北京市朝阳区xxx街道1号'),
(2, 'ORD20260430002', 4, 398.00, 398.00, 10.00, 2, 2, '李四', '13800138003', '上海市浦东新区xxx路2号'),
(3, 'ORD20260430003', 5, 14999.00, 14999.00, 0.00, 1, NULL, '王五', '13800138004', '广州市天河区xxx大道3号'),
(4, 'ORD20260430004', 3, 457.00, 457.00, 0.00, 3, 1, '张三', '13800138002', '北京市朝阳区xxx街道1号'),
(5, 'ORD20260430005', 4, 68.00, 78.00, 10.00, 0, NULL, '李四', '13800138003', '上海市浦东新区xxx路2号');

-- 9. 初始化订单项数据
DELETE FROM `order_item`;
INSERT INTO `order_item` (order_id, product_id, product_name, product_code, price, quantity, total_amount) VALUES
(1, 1, 'iPhone 15 Pro', 'IPHONE15PRO', 8999.00, 1, 8999.00),
(1, 4, '男士T恤', 'MEN_TSHIRT', 99.00, 1, 99.00),
(2, 5, '女士连衣裙', 'WOMEN_DRESS', 299.00, 1, 299.00),
(2, 7, '有机牛奶', 'ORGANIC_MILK', 68.00, 1, 68.00),
(3, 2, 'MacBook Pro 14', 'MACBOOK14', 14999.00, 1, 14999.00),
(4, 3, 'iPad Air', 'IPADAIR', 4799.00, 1, 4799.00),
(4, 8, '进口咖啡', 'IMPORT_COFFEE', 128.00, 1, 128.00),
(5, 7, '有机牛奶', 'ORGANIC_MILK', 68.00, 1, 68.00);
