SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_advance_salary
-- ----------------------------
DROP TABLE IF EXISTS `t_advance_salary`;
CREATE TABLE `t_advance_salary` (
                                    `dbid` int(11) NOT NULL AUTO_INCREMENT,
                                    `employee_id` int(11) DEFAULT NULL,
                                    `has_repay` int(11) DEFAULT '1' COMMENT '是否归还，0是、1否',
                                    `advance_time` varchar(31) DEFAULT NULL,
                                    `money` double DEFAULT NULL,
                                    `create_id` int(11) DEFAULT NULL,
                                    `remark` varchar(1023) DEFAULT NULL,
                                    PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_config_dev
-- ----------------------------
DROP TABLE IF EXISTS `t_config_dev`;
CREATE TABLE `t_config_dev` (
                                `dbid` int(11) NOT NULL AUTO_INCREMENT,
                                `key` varchar(255) DEFAULT NULL,
                                `value` varchar(255) DEFAULT NULL,
                                `desc` varchar(1023) DEFAULT NULL,
                                PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_config_dev
-- ----------------------------
BEGIN;
INSERT INTO `t_config_dev` VALUES (1, 'server.port', '45678', NULL);
INSERT INTO `t_config_dev` VALUES (2, 'server.context-path', '/', NULL);
INSERT INTO `t_config_dev` VALUES (3, 'spring.thymeleaf.suffix', '.html', NULL);
INSERT INTO `t_config_dev` VALUES (4, 'spring.thymeleaf.cache', 'false', NULL);
INSERT INTO `t_config_dev` VALUES (5, 'spring.datasource.type', 'com.alibaba.druid.pool.DruidDataSource', NULL);
INSERT INTO `t_config_dev` VALUES (6, 'spring.datasource.druid.max-wait', '60000', NULL);
INSERT INTO `t_config_dev` VALUES (7, 'spring.datasource.druid.min-idle', '1', NULL);
INSERT INTO `t_config_dev` VALUES (8, 'spring.datasource.druid.max-active', '100', NULL);
INSERT INTO `t_config_dev` VALUES (9, 'spring.datasource.druid.initial-size', '100', NULL);
INSERT INTO `t_config_dev` VALUES (10, 'spring.redis.database', '0', NULL);
INSERT INTO `t_config_dev` VALUES (11, 'spring.redis.host', '127.0.0.1', NULL);
INSERT INTO `t_config_dev` VALUES (12, 'spring.redis.port', '6379', NULL);
INSERT INTO `t_config_dev` VALUES (13, 'spring.redis.password', '5217', NULL);
INSERT INTO `t_config_dev` VALUES (14, 'spring.redis.timeout', '30000', NULL);
INSERT INTO `t_config_dev` VALUES (15, 'spring.redis.pool.max-active', '20', NULL);
INSERT INTO `t_config_dev` VALUES (16, 'spring.redis.jedis.pool.max-wait', '-1', NULL);
INSERT INTO `t_config_dev` VALUES (17, 'spring.redis.jedis.pool.max-idle', '8', NULL);
INSERT INTO `t_config_dev` VALUES (18, 'spring.redis.pool.min-idle', '0', NULL);
INSERT INTO `t_config_dev` VALUES (19, 'logging.config', 'classpath:logback-dev.xml', NULL);
INSERT INTO `t_config_dev` VALUES (29, 'spring.data.redis.repositories.enabled', 'false', NULL);
INSERT INTO `t_config_dev` VALUES (30, 'spring.data.jpa.repositories.enabled', 'false', NULL);
INSERT INTO `t_config_dev` VALUES (31, 'spring.transaction.rollback-on-commit-failure', 'true', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_crontab
-- ----------------------------
DROP TABLE IF EXISTS `t_crontab`;
CREATE TABLE `t_crontab` (
                             `dbid` int(11) NOT NULL AUTO_INCREMENT,
                             `_key` varchar(63) DEFAULT NULL,
                             `_desc` varchar(255) DEFAULT NULL,
                             `cron` varchar(31) DEFAULT NULL,
                             PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_crontab
-- ----------------------------
BEGIN;
INSERT INTO `t_crontab` VALUES (1, 'cronKey', 'cron的key', '0/5 * * * * ?');
COMMIT;

-- ----------------------------
-- Table structure for t_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
                              `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `name` varchar(31) DEFAULT NULL COMMENT '员工姓名',
                              `age` int(11) DEFAULT NULL COMMENT '年龄',
                              `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
                              `gender` int(11) DEFAULT NULL COMMENT '0-女、1-男',
                              `address` varchar(1023) DEFAULT NULL COMMENT '住址',
                              `contact` varchar(15) DEFAULT NULL COMMENT '联系方式',
                              `hour_salary` double DEFAULT NULL COMMENT '时薪',
                              `create_id` int(11) DEFAULT NULL COMMENT '创建人ID',
                              `create_time` varchar(31) DEFAULT NULL COMMENT '创建时间',
                              `last_modify_id` int(11) DEFAULT NULL COMMENT '最后修改人ID',
                              `last_modify_time` varchar(31) DEFAULT NULL COMMENT '最后修改时间',
                              PRIMARY KEY (`dbid`),
                              UNIQUE KEY `t_employee_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';

-- ----------------------------
-- Table structure for t_finance
-- ----------------------------
DROP TABLE IF EXISTS `t_finance`;
CREATE TABLE `t_finance` (
                             `dbid` int(11) NOT NULL AUTO_INCREMENT,
                             `worksite_id` int(11) DEFAULT NULL,
                             `create_id` int(11) DEFAULT NULL,
                             `create_time` varchar(31) DEFAULT NULL,
                             `last_modify_id` int(11) DEFAULT NULL,
                             `last_modify_time` varchar(31) DEFAULT NULL,
                             `money` double DEFAULT NULL,
                             `out_money` double DEFAULT NULL,
                             `del_flag` int(11) DEFAULT '0' COMMENT '0正常，1删除',
                             `del_id` int(11) DEFAULT NULL,
                             `del_time` varchar(31) DEFAULT NULL,
                             `finish_flag` int(11) DEFAULT '1' COMMENT '是否清账，0是，1否',
                             `finish_id` int(11) DEFAULT NULL,
                             `finish_time` int(11) DEFAULT NULL,
                             `remark` varchar(1023) DEFAULT NULL,
                             PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
                         `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                         `title` varchar(63) DEFAULT NULL COMMENT '标题',
                         `content` text COMMENT '内容',
                         `operator_id` int(11) DEFAULT NULL COMMENT '操作人ID',
                         `operator_name` varchar(31) DEFAULT NULL COMMENT '操作人姓名',
                         `operator_time` varchar(31) DEFAULT NULL COMMENT '操作时间',
                         `operator_ip` varchar(31) DEFAULT NULL COMMENT '操作人IP地址',
                         PRIMARY KEY (`dbid`),
                         UNIQUE KEY `t_log_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=1671 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日志表';

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
                          `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '目录表',
                          `menu_code` varchar(63) DEFAULT NULL COMMENT '目录编码',
                          `menu_name` varchar(63) DEFAULT NULL COMMENT '目录名称',
                          `pid` int(11) DEFAULT NULL COMMENT '父ID',
                          `sort` int(11) DEFAULT NULL COMMENT '排序值',
                          `url` varchar(511) DEFAULT NULL COMMENT '链接',
                          `iconCls` varchar(63) DEFAULT NULL COMMENT '图标',
                          `type` int(11) DEFAULT NULL COMMENT '1目录,2按钮',
                          `create_id` int(11) DEFAULT NULL COMMENT '创建人ID',
                          `create_time` varchar(31) DEFAULT NULL COMMENT '创建时间',
                          `last_modify_id` int(11) DEFAULT NULL COMMENT '最后修改人ID',
                          `last_modify_time` varchar(31) DEFAULT NULL COMMENT '最后修改时间',
                          `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
                          PRIMARY KEY (`dbid`),
                          UNIQUE KEY `menu_code` (`menu_code`)
) ENGINE=InnoDB AUTO_INCREMENT=668 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

-- ----------------------------
-- Records of t_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_menu` VALUES (1, 'A1', '系统设置', 0, 4, NULL, 'layui-icon-set', 1, NULL, '2018-02-09 18:15:17', 1, '2018-10-30 20:51:29', 0);
INSERT INTO `t_menu` VALUES (2, 'A1_01', '用户管理', 1, 1, '/user/index', '', 1, NULL, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
INSERT INTO `t_menu` VALUES (3, 'A1_01_01', '添加用户', 2, 1, '', '', 2, NULL, '2018-02-09 18:15:27', 1, '2019-06-13 18:33:44', 0);
INSERT INTO `t_menu` VALUES (4, 'A1_01_02', '修改用户', 2, 2, NULL, '', 2, NULL, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:21', 0);
INSERT INTO `t_menu` VALUES (5, 'A1_01_03', '删除用户', 2, 3, NULL, '', 2, NULL, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:26', 0);
INSERT INTO `t_menu` VALUES (6, 'A1_01_04', '修改用户状态', 2, 4, NULL, NULL, 2, NULL, '2018-02-09 18:15:27', 1, '2018-10-30 15:43:24', 0);
INSERT INTO `t_menu` VALUES (7, 'A1_01_05', '设置用户角色', 2, 5, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (8, 'A1_02', '菜单管理', 1, 3, '/menu/index', '', 1, NULL, '2018-02-09 18:15:27', 1, '2018-10-30 20:52:38', 0);
INSERT INTO `t_menu` VALUES (9, 'A1_02_01', '修改名称', 8, 1, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (10, 'A1_02_02', '修改菜单状态', 8, 2, NULL, NULL, 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (11, 'A1_03', '角色管理', 1, 2, '/role/index', '', 1, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (12, 'A1_03_01', '添加角色', 11, 1, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (13, 'A1_03_02', '修改角色', 11, 2, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (14, 'A1_03_03', '删除角色', 11, 3, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (15, 'A1_03_04', '修改角色状态', 11, 4, NULL, NULL, 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (16, 'A1_03_05', '设置角色权限', 11, 5, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (17, 'A1_04', '系统日志', 1, 4, '/log/index', '', 1, NULL, '2018-02-09 18:15:27', 1, '2018-10-30 20:49:15', 0);
INSERT INTO `t_menu` VALUES (18, 'B1', '员工管理', 0, 1, NULL, 'layui-icon-user', 1, NULL, '2018-02-09 18:15:27', 1, '2018-10-31 09:25:38', 0);
INSERT INTO `t_menu` VALUES (19, 'B1_01', '员工管理', 18, 1, '/employee/index', '', 1, NULL, '2018-02-09 18:15:27', NULL, '2018-08-21 14:59:32', 0);
INSERT INTO `t_menu` VALUES (20, 'B1_01_01', '添加员工', 19, 1, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (21, 'B1_01_02', '修改员工', 19, 2, NULL, '', 2, NULL, '2018-02-09 18:15:27', 1, '2019-09-19 15:41:49', 0);
INSERT INTO `t_menu` VALUES (22, 'B1_01_03', '删除员工', 19, 3, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (23, 'B1_01_04', '修改员工状态', 19, 4, NULL, NULL, 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (24, 'B1_01_05', '显示金额', 19, 5, NULL, '', 2, NULL, '2018-02-09 18:15:27', NULL, '2018-02-09 18:15:27', 0);
INSERT INTO `t_menu` VALUES (25, 'B1_02', '工时管理', 18, 3, '/workhour/index', '', 1, NULL, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:14', 0);
INSERT INTO `t_menu` VALUES (26, 'B1_02_01', '工时录入', 25, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (27, 'B1_02_02', '删除工时', 25, 2, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (28, 'B1_02_03', '显示金额', 25, 3, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (29, 'B1_03', '工地管理', 18, 2, '/worksite/index', '', 1, NULL, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:08', 0);
INSERT INTO `t_menu` VALUES (30, 'B1_03_01', '添加工地', 29, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (31, 'B1_03_02', '修改工地', 29, 2, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (32, 'B1_03_03', '删除工地', 29, 3, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (33, 'B1_03_04', '修改工地状态', 29, 4, NULL, NULL, 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (34, 'C1', '工资管理', 0, 2, NULL, 'layui-icon-rmb', 1, NULL, '2018-08-21 18:15:27', 1, '2019-09-22 14:13:54', 0);
INSERT INTO `t_menu` VALUES (35, 'C1_01', '薪资发放', 34, 1, '/pay/index', '', 1, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (36, 'C1_01_01', '更改实发金额', 35, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (37, 'C1_02', '发薪记录', 34, 2, '/payRecord/index', '', 1, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (38, 'C1_02_01', '显示金额', 37, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (39, 'C1_04', '财务管理', 34, 4, '/finance/index', '', 1, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (40, 'C1_05', '薪资统计', 34, 5, '/salary/index', '', 1, NULL, '2018-08-21 18:15:27', 1, '2019-09-26 16:39:37', 0);
INSERT INTO `t_menu` VALUES (41, 'C1_03', '预支薪资', 34, 3, '/advanceSalary/index', '', 1, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (42, 'C1_03_01', '添加预支薪资', 41, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (43, 'C1_03_02', '删除预支薪资', 41, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (44, 'C1_03_03', '显示金额', 41, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (45, 'C1_04_01', '财务登记', 39, 1, NULL, '', 2, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (46, 'C1_04_02', '显示金额', 39, 3, NULL, '', 2, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (47, 'C1_04_03', '删除财务登记', 39, 2, NULL, '', 2, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (48, 'B1_02_04', '显示总工时', 25, 4, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (49, 'C1_01_02', '查看薪资总额', 35, 2, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (50, 'C1_02_02', '显示总金额', 37, 2, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (51, 'C1_03_04', '显示预支总额', 41, 4, NULL, '', 2, NULL, '2018-08-21 18:15:27', NULL, '2018-08-21 18:15:27', 0);
INSERT INTO `t_menu` VALUES (52, 'C1_04_04', '显示总登记金额', 39, 4, NULL, '', 2, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (53, 'C1_04_05', '显示总支出金额', 39, 5, NULL, '', 2, NULL, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
INSERT INTO `t_menu` VALUES (54, 'B1_04', '工时报表', 18, 4, '/workhourReport/index', '', 1, NULL, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:08', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_record`;
CREATE TABLE `t_pay_record` (
                                `dbid` int(11) NOT NULL AUTO_INCREMENT,
                                `payer_id` int(11) DEFAULT NULL,
                                `employee_id` int(11) DEFAULT NULL,
                                `pay_date` varchar(31) DEFAULT NULL,
                                `total_money` double DEFAULT NULL COMMENT '总薪资',
                                `total_hour` int(11) DEFAULT NULL COMMENT '总工时',
                                `workhour_ids` varchar(255) DEFAULT NULL COMMENT '工时ID列表',
                                `remark` text,
                                `has_modify_money` int(11) DEFAULT NULL COMMENT '是否修改了金额，1是，0否',
                                `change_money` double DEFAULT NULL,
                                PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='发薪记录表';

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
                          `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `role_name` varchar(31) DEFAULT NULL COMMENT '角色名称',
                          `create_time` varchar(31) DEFAULT NULL COMMENT '创建时间',
                          `create_id` int(11) DEFAULT NULL COMMENT '创建人ID',
                          `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
                          `last_modify_time` varchar(31) DEFAULT NULL COMMENT '最后修改时间',
                          `last_modify_id` int(11) DEFAULT NULL COMMENT '最后修改人ID',
                          PRIMARY KEY (`dbid`),
                          UNIQUE KEY `t_role_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` VALUES (1, '超级管理员', '2018-10-19 12:52:17', 1, 0, '2019-02-06 17:04:26', 1);
INSERT INTO `t_role` VALUES (17, '普通用户', '2018-10-26 01:15:48', 1, 0, '2019-09-25 22:54:15', 1);
INSERT INTO `t_role` VALUES (19, '管理员', '2019-05-14 15:10:11', 1, 0, '2019-09-25 23:11:53', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
                               `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
                               `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
                               PRIMARY KEY (`dbid`),
                               UNIQUE KEY `t_role_menu_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=3040 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-菜单关系表';

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_role_menu` VALUES (2986, 1, 18);
INSERT INTO `t_role_menu` VALUES (2987, 1, 19);
INSERT INTO `t_role_menu` VALUES (2988, 1, 20);
INSERT INTO `t_role_menu` VALUES (2989, 1, 21);
INSERT INTO `t_role_menu` VALUES (2990, 1, 22);
INSERT INTO `t_role_menu` VALUES (2991, 1, 23);
INSERT INTO `t_role_menu` VALUES (2992, 1, 24);
INSERT INTO `t_role_menu` VALUES (2993, 1, 29);
INSERT INTO `t_role_menu` VALUES (2994, 1, 30);
INSERT INTO `t_role_menu` VALUES (2995, 1, 31);
INSERT INTO `t_role_menu` VALUES (2996, 1, 32);
INSERT INTO `t_role_menu` VALUES (2997, 1, 33);
INSERT INTO `t_role_menu` VALUES (2998, 1, 25);
INSERT INTO `t_role_menu` VALUES (2999, 1, 26);
INSERT INTO `t_role_menu` VALUES (3000, 1, 27);
INSERT INTO `t_role_menu` VALUES (3001, 1, 28);
INSERT INTO `t_role_menu` VALUES (3002, 1, 48);
INSERT INTO `t_role_menu` VALUES (3003, 1, 54);
INSERT INTO `t_role_menu` VALUES (3004, 1, 34);
INSERT INTO `t_role_menu` VALUES (3005, 1, 35);
INSERT INTO `t_role_menu` VALUES (3006, 1, 36);
INSERT INTO `t_role_menu` VALUES (3007, 1, 49);
INSERT INTO `t_role_menu` VALUES (3008, 1, 37);
INSERT INTO `t_role_menu` VALUES (3009, 1, 38);
INSERT INTO `t_role_menu` VALUES (3010, 1, 50);
INSERT INTO `t_role_menu` VALUES (3011, 1, 41);
INSERT INTO `t_role_menu` VALUES (3012, 1, 42);
INSERT INTO `t_role_menu` VALUES (3013, 1, 43);
INSERT INTO `t_role_menu` VALUES (3014, 1, 44);
INSERT INTO `t_role_menu` VALUES (3015, 1, 51);
INSERT INTO `t_role_menu` VALUES (3016, 1, 39);
INSERT INTO `t_role_menu` VALUES (3017, 1, 45);
INSERT INTO `t_role_menu` VALUES (3018, 1, 47);
INSERT INTO `t_role_menu` VALUES (3019, 1, 46);
INSERT INTO `t_role_menu` VALUES (3020, 1, 52);
INSERT INTO `t_role_menu` VALUES (3021, 1, 53);
INSERT INTO `t_role_menu` VALUES (3022, 1, 40);
INSERT INTO `t_role_menu` VALUES (3023, 1, 1);
INSERT INTO `t_role_menu` VALUES (3024, 1, 2);
INSERT INTO `t_role_menu` VALUES (3025, 1, 3);
INSERT INTO `t_role_menu` VALUES (3026, 1, 4);
INSERT INTO `t_role_menu` VALUES (3027, 1, 5);
INSERT INTO `t_role_menu` VALUES (3028, 1, 6);
INSERT INTO `t_role_menu` VALUES (3029, 1, 7);
INSERT INTO `t_role_menu` VALUES (3030, 1, 11);
INSERT INTO `t_role_menu` VALUES (3031, 1, 12);
INSERT INTO `t_role_menu` VALUES (3032, 1, 13);
INSERT INTO `t_role_menu` VALUES (3033, 1, 14);
INSERT INTO `t_role_menu` VALUES (3034, 1, 15);
INSERT INTO `t_role_menu` VALUES (3035, 1, 16);
INSERT INTO `t_role_menu` VALUES (3036, 1, 8);
INSERT INTO `t_role_menu` VALUES (3037, 1, 9);
INSERT INTO `t_role_menu` VALUES (3038, 1, 10);
INSERT INTO `t_role_menu` VALUES (3039, 1, 17);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dic`;
CREATE TABLE `t_sys_dic` (
                             `dbid` int(11) NOT NULL AUTO_INCREMENT,
                             `_key` varchar(1024) DEFAULT NULL,
                             `_value` varchar(1024) DEFAULT NULL,
                             `_profile` varchar(31) DEFAULT NULL,
                             `_desc` varchar(1024) DEFAULT NULL,
                             PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_sys_dic
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_dic` VALUES (1, 'name', 'Oven', NULL, NULL);
INSERT INTO `t_sys_dic` VALUES (2, 'age', '18', NULL, NULL);
INSERT INTO `t_sys_dic` VALUES (3, 'gender', '男', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
                          `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `user_name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
                          `password` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
                          `nick_name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
                          `age` int(11) DEFAULT NULL COMMENT '年龄',
                          `email` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
                          `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                          `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
                          `gender` int(11) DEFAULT NULL COMMENT '0-女、1-男',
                          `create_time` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建时间',
                          `create_id` int(11) DEFAULT NULL COMMENT '创建人ID',
                          `last_modify_time` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后修改时间',
                          `last_modify_id` int(11) DEFAULT NULL COMMENT '最后修改人ID',
                          PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, 'Oven', '96ba8efe664c1b11fa56a691e1094cc8', 'Oven', 18, '123@163.com', '15712345678', 0, 1, '2018-10-19 12:52:17', 1, '2018-10-29 20:32:18', 1);
INSERT INTO `t_user` VALUES (8, 'admin', 'd8e6ca2238dccaf93c13bde915f9e88f', 'admin', 18, '123@163.com', '15712345678', 0, 1, '2018-10-30 09:49:43', 1, '2019-06-14 11:11:46', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
                               `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
                               `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
                               PRIMARY KEY (`dbid`),
                               UNIQUE KEY `t_user_role_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-角色关系表';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
BEGIN;
INSERT INTO `t_user_role` VALUES (30, 1, 1);
INSERT INTO `t_user_role` VALUES (31, 8, 17);
COMMIT;

-- ----------------------------
-- Table structure for t_workhour
-- ----------------------------
DROP TABLE IF EXISTS `t_workhour`;
CREATE TABLE `t_workhour` (
                              `dbid` int(11) NOT NULL AUTO_INCREMENT,
                              `employee_id` int(11) DEFAULT NULL COMMENT '员工ID',
                              `worksite_id` int(11) DEFAULT NULL COMMENT '工地ID',
                              `work_date` varchar(31) DEFAULT NULL COMMENT '工时日期',
                              `work_hour` int(11) DEFAULT NULL COMMENT '录入工时',
                              `hour_salary` double DEFAULT NULL COMMENT '录入工时时的时薪',
                              `create_id` int(11) DEFAULT NULL,
                              `create_time` varchar(31) DEFAULT NULL,
                              `has_pay` int(11) DEFAULT '0' COMMENT '是否发薪，0否，1是',
                              `remark` text COMMENT '备注',
                              PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for t_worksite
-- ----------------------------
DROP TABLE IF EXISTS `t_worksite`;
CREATE TABLE `t_worksite` (
                              `dbid` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(127) DEFAULT NULL,
                              `desc` text,
                              `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
                              `create_id` int(11) DEFAULT NULL,
                              `create_time` varchar(31) DEFAULT NULL,
                              `last_modify_id` int(11) DEFAULT NULL,
                              `last_modify_time` varchar(31) DEFAULT NULL,
                              PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
