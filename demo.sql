-- MySQL dump 10.13  Distrib 8.0.14, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: db_demo
-- ------------------------------------------------------
-- Server version	8.0.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_config_dev`
--

DROP TABLE IF EXISTS `t_config_dev`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_config_dev` (
  `dbid` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `desc` varchar(1023) DEFAULT NULL,
  PRIMARY KEY (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_config_dev`
--

LOCK TABLES `t_config_dev` WRITE;
/*!40000 ALTER TABLE `t_config_dev` DISABLE KEYS */;
INSERT INTO `t_config_dev` (`dbid`, `key`, `value`, `desc`) VALUES (1,'server.port','45678',NULL),(2,'server.context-path','/',NULL),(3,'spring.thymeleaf.suffix','.html',NULL),(4,'spring.thymeleaf.cache','false',NULL),(6,'spring.datasource.type','com.alibaba.druid.pool.DruidDataSource',NULL),(7,'spring.datasource.initialSize','20',NULL),(8,'spring.datasource.minIdle','10',NULL),(9,'spring.datasource.maxActive','100',NULL),(10,'spring.redis.database','0',NULL),(11,'spring.redis.host','172.16.188.133',NULL),(12,'spring.redis.port','6379',NULL),(13,'spring.redis.password','5217',NULL),(14,'spring.redis.timeout','30000',NULL),(15,'spring.redis.pool.max-active','20',NULL),(16,'spring.redis.pool.max-wait','-1',NULL),(17,'spring.redis.pool.max-idle','8',NULL),(18,'spring.redis.pool.min-idle','0',NULL),(19,'logging.config','classpath:logback-dev.xml',NULL);
/*!40000 ALTER TABLE `t_config_dev` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_employee`
--

DROP TABLE IF EXISTS `t_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_employee` (
  `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(31) DEFAULT NULL COMMENT '员工姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
  `gender` int(11) DEFAULT NULL COMMENT '0-女、1-男',
  `address` varchar(1023) DEFAULT NULL COMMENT '住址',
  `contact` varchar(15) DEFAULT NULL COMMENT '联系方式',
  `day_salary` double DEFAULT NULL COMMENT '日薪',
  `month_salary` double DEFAULT NULL COMMENT '月薪',
  `create_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_time` varchar(31) DEFAULT NULL COMMENT '创建时间',
  `last_modify_id` int(11) DEFAULT NULL COMMENT '最后修改人ID',
  `last_modify_time` varchar(31) DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`dbid`),
  UNIQUE KEY `t_employee_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_employee`
--

LOCK TABLES `t_employee` WRITE;
/*!40000 ALTER TABLE `t_employee` DISABLE KEYS */;
INSERT INTO `t_employee` (`dbid`, `name`, `age`, `status`, `gender`, `address`, `contact`, `day_salary`, `month_salary`, `create_id`, `create_time`, `last_modify_id`, `last_modify_time`) VALUES (1,'张三',18,0,1,'河北邯郸','12345678901',140,4500,1,'2018-11-01 11:40:09',1,'2018-11-01 13:50:13'),(3,'李四',19,0,0,'北京','12345678901',180,5500,1,'2018-11-01 13:51:49',1,'2018-11-01 14:07:01');
/*!40000 ALTER TABLE `t_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=1021 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` (`dbid`, `title`, `content`, `operator_id`, `operator_name`, `operator_time`, `operator_ip`) VALUES (1,'登录系统！','成功！',1,'Oven','2019-02-06 17:04:18','');
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_menu`
--

LOCK TABLES `t_menu` WRITE;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` (`dbid`, `menu_code`, `menu_name`, `pid`, `sort`, `url`, `iconCls`, `type`, `create_id`, `create_time`, `last_modify_id`, `last_modify_time`, `status`) VALUES (1,'A1','系统设置',0,1,NULL,'layui-icon-set',1,NULL,'2018-02-09 18:15:17',1,'2018-10-30 20:51:29',0),(2,'A1_01','用户管理',1,1,'/user/index','',1,NULL,'2018-02-09 18:15:27',1,'2018-10-30 20:51:42',0),(3,'A1_01_01','添加用户',2,1,'','',2,NULL,'2018-02-09 18:15:27',1,'2018-11-14 15:44:30',1),(4,'A1_01_02','修改用户',2,2,NULL,'',2,NULL,'2018-02-09 18:15:27',1,'2018-10-30 15:47:21',0),(5,'A1_01_03','删除用户',2,3,NULL,'',2,NULL,'2018-02-09 18:15:27',1,'2018-10-30 15:47:26',0),(6,'A1_01_04','修改用户状态',2,4,NULL,NULL,2,NULL,'2018-02-09 18:15:27',1,'2018-10-30 15:43:24',0),(7,'A1_01_05','设置用户角色',2,5,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(8,'A1_02','菜单管理',1,3,'/menu/index','',1,NULL,'2018-02-09 18:15:27',1,'2018-10-30 20:52:38',0),(9,'A1_02_01','修改名称',8,1,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(10,'A1_02_02','修改菜单状态',8,2,NULL,NULL,2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(11,'A1_03','角色管理',1,2,'/role/index','',1,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(12,'A1_03_01','添加角色',11,1,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(13,'A1_03_02','修改角色',11,2,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(14,'A1_03_03','删除角色',11,3,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(15,'A1_03_04','修改角色状态',11,4,NULL,NULL,2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(16,'A1_03_05','设置角色权限',11,5,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(17,'A1_04','系统日志',1,4,'/log/index','',1,NULL,'2018-02-09 18:15:27',1,'2018-10-30 20:49:15',0),(18,'B1','员工管理',0,2,NULL,'layui-icon-user',1,NULL,'2018-02-09 18:15:27',1,'2018-10-31 09:25:38',0),(19,'B1_01','员工管理',18,1,'/employee/index','',1,NULL,'2018-02-09 18:15:27',NULL,'2018-08-21 14:59:32',0),(20,'B1_01_01','添加员工',19,1,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(21,'B1_01_02','修改员工',19,2,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-03-09 15:31:40',0),(22,'B1_01_03','删除员工',19,3,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(23,'B1_01_04','修改员工状态',19,4,NULL,NULL,2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(24,'B1_01_05','显示金额',19,5,NULL,'',2,NULL,'2018-02-09 18:15:27',NULL,'2018-02-09 18:15:27',0),(25,'B1_02','工时管理',18,2,NULL,'',1,NULL,'2018-08-21 18:15:27',1,'2018-11-01 14:45:14',0),(26,'B1_02_01','工时查询',25,1,'/workinghour/workinghour/index.html','',1,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(27,'B1_02_02','工时录入',25,2,'/workinghour/workinghour/add.html','',1,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(28,'B1_03','工地管理',18,3,NULL,'',1,NULL,'2018-08-21 18:15:27',1,'2018-11-01 14:45:08',0),(29,'B1_03_01','添加工地',28,1,NULL,'',2,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(30,'B1_03_02','修改工地',28,2,NULL,'',2,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(31,'B1_03_03','删除工地',28,3,NULL,'',2,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(32,'B1_03_04','修改工地状态',28,4,NULL,NULL,2,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(33,'C1','工资统计',0,3,NULL,'layui-icon-rmb',1,NULL,'2018-08-21 18:15:27',1,'2019-01-09 14:04:08',0),(34,'C1_01','工资统计',33,0,'/salary/salary/index.html','',1,NULL,'2018-08-21 18:15:27',1,'2019-01-10 20:28:56',0),(35,'C1_01_01','工资发放',34,0,NULL,'',2,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0),(36,'C1_02','发薪记录',33,1,'/paySalary/paySalary/index.html','',1,NULL,'2018-08-21 18:15:27',NULL,'2018-08-21 18:15:27',0);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`dbid`, `role_name`, `create_time`, `create_id`, `status`, `last_modify_time`, `last_modify_id`) VALUES (1,'超级管理员','2018-10-19 12:52:17',1,0,'2019-02-06 17:04:26',1),(17,'普通用户','2018-10-26 01:15:48',1,0,'2018-10-31 09:25:19',1);
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role_menu`
--

DROP TABLE IF EXISTS `t_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_role_menu` (
  `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`dbid`),
  UNIQUE KEY `t_role_menu_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=730 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role_menu`
--

LOCK TABLES `t_role_menu` WRITE;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;
INSERT INTO `t_role_menu` (`dbid`, `role_id`, `menu_id`) VALUES (694,1,1),(695,1,2),(696,1,3),(697,1,4),(698,1,5),(699,1,6),(700,1,7),(701,1,11),(702,1,12),(703,1,13),(704,1,14),(705,1,15),(706,1,16),(707,1,8),(708,1,9),(709,1,10),(710,1,17),(711,1,18),(712,1,19),(713,1,20),(714,1,21),(715,1,22),(716,1,23),(717,1,24),(718,1,25),(719,1,26),(720,1,27),(721,1,28),(722,1,29),(723,1,30),(724,1,31),(725,1,32),(726,1,33),(727,1,34),(728,1,35),(729,1,36);
/*!40000 ALTER TABLE `t_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`dbid`, `user_name`, `password`, `nick_name`, `age`, `email`, `phone`, `status`, `gender`, `create_time`, `create_id`, `last_modify_time`, `last_modify_id`) VALUES (1,'Oven','96ba8efe664c1b11fa56a691e1094cc8','Oven',18,'oven@vhzsqm.com','12345678909',0,1,'2018-10-19 12:52:17',1,'2018-10-29 20:32:18',1), (8,'admin','d8e6ca2238dccaf93c13bde915f9e88f','admin',18,'admin@vhzsqm.com','12345678909',0,1,'2018-10-30 09:49:43',1,'2018-11-01 13:59:14',1);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_role`
--

DROP TABLE IF EXISTS `t_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_user_role` (
  `dbid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`dbid`),
  UNIQUE KEY `t_user_role_dbid_uindex` (`dbid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_role`
--

LOCK TABLES `t_user_role` WRITE;
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` (`dbid`, `user_id`, `role_id`) VALUES (30,1,1),(31,8,17);
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_worksite`
--

DROP TABLE IF EXISTS `t_worksite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_worksite` (
  `dbid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(127) DEFAULT NULL,
  `desc` text,
  `status` int(11) DEFAULT NULL COMMENT '状态，0-正常、1-删除',
  `create_id` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `last_modify_id` int(11) DEFAULT NULL,
  `last_modify_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`dbid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_worksite`
--

LOCK TABLES `t_worksite` WRITE;
/*!40000 ALTER TABLE `t_worksite` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_worksite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-01 19:02:53
