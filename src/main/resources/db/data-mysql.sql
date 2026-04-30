DELETE FROM `user`;

INSERT INTO `user` (id, name, age, email, username, password) VALUES
                                              (1, 'Jone', 18, 'test1@baomidou.com', 'admin', '123456'),
                                              (2, 'Jack', 20, 'test2@baomidou.com', 'jack', '123456'),
                                              (3, 'Tom', 28, 'test3@baomidou.com', 'tom', '123456'),
                                              (4, 'Sandy', 21, 'test4@baomidou.com', 'sandy', '123456'),
                                              (5, 'Billie', 24, 'test5@baomidou.com', 'billie', '123456');

DELETE FROM `role`;

INSERT INTO `role` (id, role_name, role_code, description) VALUES
                                                               (1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限'),
                                                               (2, '普通管理员', 'ADMIN', '基本管理权限'),
                                                               (3, '普通用户', 'USER', '基本使用权限');

DELETE FROM `menu`;

INSERT INTO `menu` (id, menu_name, menu_code, parent_id, path, sort, icon) VALUES
                                                                                (1, '用户管理', 'USER_MANAGE', '0', '/admin/users', 1, 'bi-people-fill'),
                                                                                (2, '角色管理', 'ROLE_MANAGE', '0', '/admin/role', 2, 'bi-shield-lock-fill'),
                                                                                (3, '菜单管理', 'MENU_MANAGE', '0', '/admin/menu', 3, 'bi-menu-button-fill');
