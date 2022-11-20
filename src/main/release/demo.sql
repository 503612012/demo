create table t_config
(
    dbid   int auto_increment primary key,
    `key`  varchar(255)  null,
    value  varchar(255)  null,
    `desc` varchar(1023) null
);

create table t_crontab
(
    dbid  int auto_increment primary key,
    _key  varchar(63)  null,
    _desc varchar(255) null,
    cron  varchar(31)  null
);

create table t_employee
(
    dbid             int auto_increment comment '主键' primary key,
    name             varchar(31)   null comment '员工姓名',
    age              int           null comment '年龄',
    status           int           null comment '状态，0-正常、1-删除',
    gender           int           null comment '0-女、1-男',
    address          varchar(1023) null comment '住址',
    contact          varchar(15)   null comment '联系方式',
    hour_salary      double        null comment '时薪',
    create_id        int           null comment '创建人ID',
    create_time      varchar(31)   null comment '创建时间',
    last_modify_id   int           null comment '最后修改人ID',
    last_modify_time varchar(31)   null comment '最后修改时间',
    constraint t_employee_dbid_uindex unique (dbid)
) comment '员工表';

create table db_demo.t_log
(
    dbid           int auto_increment comment '主键' primary key,
    title          varchar(63)   null comment '标题',
    request        varchar(5000) null comment '内容',
    response       varchar(5000) null,
    request_uri    varchar(127)  null,
    request_method varchar(15)   null,
    operator_id    int           null comment '操作人ID',
    operator_name  varchar(31)   null comment '操作人姓名',
    operator_time  varchar(31)   null comment '操作时间',
    operator_ip    varchar(31)   null comment '操作人IP地址',
    constraint t_log_dbid_uindex unique (dbid)
) comment '日志表';

create table t_menu
(
    dbid             int auto_increment comment '目录表' primary key,
    menu_code        varchar(63)  null comment '目录编码',
    menu_name        varchar(63)  null comment '目录名称',
    pid              int          null comment '父ID',
    sort             int          null comment '排序值',
    url              varchar(511) null comment '链接',
    iconCls          varchar(63)  null comment '图标',
    type             int          null comment '1目录,2按钮',
    create_id        int          null comment '创建人ID',
    create_time      varchar(31)  null comment '创建时间',
    last_modify_id   int          null comment '最后修改人ID',
    last_modify_time varchar(31)  null comment '最后修改时间',
    status           int          null comment '状态，0-正常、1-删除',
    constraint menu_code unique (menu_code)
) comment '菜单表';

create table t_request_log_template
(
    request_time   varchar(31)   null comment '请求时间',
    request_url    varchar(1023) null comment '请求地址',
    request_method varchar(31)   null comment '请求方法',
    request_ip     varchar(127)  null comment '请求者IP',
    request_param  text          null comment '请求参数',
    user_id        int           null comment '登录人ID'
) comment '接口请求日志模板表';

create table t_role
(
    dbid             int auto_increment comment '主键' primary key,
    role_name        varchar(31) null comment '角色名称',
    create_time      varchar(31) null comment '创建时间',
    create_id        int         null comment '创建人ID',
    status           int         null comment '状态，0-正常、1-删除',
    last_modify_time varchar(31) null comment '最后修改时间',
    last_modify_id   int         null comment '最后修改人ID',
    constraint t_role_dbid_uindex unique (dbid)
) comment '角色表';

create table t_role_menu
(
    dbid    int auto_increment comment '主键' primary key,
    role_id int null comment '角色ID',
    menu_id int null comment '菜单ID',
    constraint t_role_menu_dbid_uindex unique (dbid)
) comment '角色-菜单关系表';

create table t_sys_dic
(
    dbid     int auto_increment primary key,
    _key     varchar(1024) null,
    _value   varchar(1024) null,
    _profile varchar(31)   null,
    _desc    varchar(1024) null
);

create table t_sys_filter
(
    env         varchar(32)   not null comment '运行环境，如生产、开发环境',
    _name       varchar(32)   not null comment '过滤器名称',
    _class      varchar(256)  null comment '过滤器类',
    url_pattern varchar(256)  null comment '拦截的url地址',
    params      varchar(2047) null comment '过滤器参数',
    order_no    int(32)       null comment '序号'
) comment '系统过滤器配置';

create table t_user
(
    dbid             int auto_increment comment '主键' primary key,
    user_name        varchar(31)  null comment '用户名',
    password         varchar(63)  null comment '密码',
    nick_name        varchar(31)  null comment '昵称',
    age              int          null comment '年龄',
    email            varchar(63)  null comment '邮箱',
    phone            varchar(11)  null comment '手机号',
    status           int          null comment '状态，0-正常、1-删除',
    gender           int          null comment '0-女、1-男',
    create_time      varchar(31)  null comment '创建时间',
    create_id        int          null comment '创建人ID',
    last_modify_time varchar(31)  null comment '最后修改时间',
    last_modify_id   int          null comment '最后修改人ID',
    last_login_time  varchar(31)  null comment '最后登录时间',
    open_id          varchar(127) null,
    err_num          int          null,
    avatar           varchar(127) null,
    config           varchar(511) null
) comment '用户表';

create table t_user_role
(
    dbid    int auto_increment comment '主键' primary key,
    user_id int null comment '用户ID',
    role_id int null comment '角色ID',
    constraint t_user_role_dbid_uindex unique (dbid)
) comment '用户-角色关系表';

insert into t_config (dbid, `key`, value, `desc`) values (1, 'server.port', '45678', null);
insert into t_config (dbid, `key`, value, `desc`) values (2, 'server.servlet.context-path', '/', null);
insert into t_config (dbid, `key`, value, `desc`) values (3, 'spring.thymeleaf.cache', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (4, 'spring.datasource.type', 'com.alibaba.druid.pool.DruidDataSource', null);
insert into t_config (dbid, `key`, value, `desc`) values (5, 'spring.datasource.druid.initial-size', '1', null);
insert into t_config (dbid, `key`, value, `desc`) values (6, 'spring.datasource.druid.max-active', '20', null);
insert into t_config (dbid, `key`, value, `desc`) values (7, 'spring.datasource.druid.min-idle', '1', null);
insert into t_config (dbid, `key`, value, `desc`) values (8, 'spring.datasource.druid.max-wait', '1000', null);
insert into t_config (dbid, `key`, value, `desc`) values (9, 'spring.datasource.druid.pool-prepared-statements', 'true', null);
insert into t_config (dbid, `key`, value, `desc`) values (10, 'spring.datasource.druid.max-open-prepared-statements', '20', null);
insert into t_config (dbid, `key`, value, `desc`) values (11, 'spring.datasource.druid.validation-query', 'select 1 from dual', null);
insert into t_config (dbid, `key`, value, `desc`) values (12, 'spring.datasource.druid.validation-query-timeout', '50000', null);
insert into t_config (dbid, `key`, value, `desc`) values (13, 'spring.datasource.druid.test-on-borrow', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (14, 'spring.datasource.druid.test-on-return', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (15, 'spring.datasource.druid.test-while-idle', 'true', null);
insert into t_config (dbid, `key`, value, `desc`) values (16, 'spring.datasource.druid.time-between-eviction-runs-millis', '60000', null);
insert into t_config (dbid, `key`, value, `desc`) values (17, 'spring.datasource.druid.min-evictable-idle-time-millis', '30000', null);
insert into t_config (dbid, `key`, value, `desc`) values (18, 'spring.datasource.druid.max-evictable-idle-time-millis', '60000', null);
insert into t_config (dbid, `key`, value, `desc`) values (19, 'spring.datasource.druid.remove-abandoned', 'true', null);
insert into t_config (dbid, `key`, value, `desc`) values (20, 'spring.datasource.druid.remove-abandoned-timeout', '1800', null);
insert into t_config (dbid, `key`, value, `desc`) values (21, 'spring.datasource.druid.connection-properties', 'druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000', null);
insert into t_config (dbid, `key`, value, `desc`) values (22, 'spring.datasource.druid.max-pool-prepared-statement-per-connection-size', '20', null);
insert into t_config (dbid, `key`, value, `desc`) values (23, 'spring.datasource.druid.stat-view-servlet.reset-enable', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (24, 'spring.datasource.druid.filters', 'stat,wall', null);
insert into t_config (dbid, `key`, value, `desc`) values (25, 'spring.redis.database', '0', null);
insert into t_config (dbid, `key`, value, `desc`) values (26, 'spring.redis.host', '127.0.0.1', null);
insert into t_config (dbid, `key`, value, `desc`) values (27, 'spring.redis.port', '6379', null);
insert into t_config (dbid, `key`, value, `desc`) values (28, 'spring.redis.password', '5217', null);
insert into t_config (dbid, `key`, value, `desc`) values (29, 'spring.redis.timeout', '30000', null);
insert into t_config (dbid, `key`, value, `desc`) values (30, 'spring.redis.pool.max-active', '20', null);
insert into t_config (dbid, `key`, value, `desc`) values (31, 'spring.redis.jedis.pool.max-wait', '-1', null);
insert into t_config (dbid, `key`, value, `desc`) values (32, 'spring.redis.jedis.pool.max-idle', '8', null);
insert into t_config (dbid, `key`, value, `desc`) values (33, 'spring.redis.pool.min-idle', '0', null);
insert into t_config (dbid, `key`, value, `desc`) values (34, 'logging.config', 'classpath:logback-dev.xml', null);
insert into t_config (dbid, `key`, value, `desc`) values (35, 'spring.data.redis.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (36, 'spring.data.jpa.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (37, 'spring.transaction.rollback-on-commit-failure', 'true', null);
insert into t_config (dbid, `key`, value, `desc`) values (38, 'avatar.path', '/Users/oven/logs/img/avatar/', '头像保存地址');
insert into t_config (dbid, `key`, value, `desc`) values (39, 'basic.table.prefix', 't', '表名前缀');

insert into t_crontab (dbid, _key, _desc, cron) values (1, 'REQUEST_LOG_CRON', '每分钟将队列中的请求日志保存到数据库', '0 */1 * * * ?');
insert into t_crontab (dbid, _key, _desc, cron) values (2, 'LOG_CRON', '每分钟将队列中的操作日志保存到数据库', '0 */1 * * * ?');

insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (1, 'sys', '系统设置', 0, 8, null, 'layui-icon-set', 1, null, '2018-02-09 18:15:17', 1, '2018-10-30 20:51:29', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (2, 'sys:user', '用户管理', 1, 2, '/user/index', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (3, 'sys:user:add', '添加用户', 2, 1, null, '', 2, null, '2018-02-09 18:15:27', 1, '2019-06-13 18:33:44', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (4, 'sys:user:update', '修改用户', 2, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:21', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (5, 'sys:user:del', '删除用户', 2, 3, null, '', 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:26', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (6, 'sys:user:status', '修改用户状态', 2, 4, null, null, 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:43:24', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (7, 'sys:user:setrole', '设置用户角色', 2, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (8, 'sys:user:forcelogout', '强制退出', 2, 6, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (9, 'sys:user:reset', '重置错误次数', 2, 7, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (10, 'sys:user:avatar', '修改头像', 2, 8, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (11, 'sys:user:theme', '修改主题', 2, 9, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (12, 'sys:user:menuposition', '菜单位置', 2, 10, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (13, 'sys:menu', '菜单管理', 1, 6, '/menu/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:52:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (14, 'sys:menu:update', '修改名称', 13, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (15, 'sys:menu:status', '修改菜单状态', 13, 2, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (16, 'sys:role', '角色管理', 1, 3, '/role/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (17, 'sys:role:add', '添加角色', 16, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (18, 'sys:role:update', '修改角色', 16, 2, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (19, 'sys:role:del', '删除角色', 16, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (20, 'sys:role:status', '修改角色状态', 16, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (21, 'sys:role:setmenu', '设置角色权限', 16, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (22, 'sys:log', '日志查看', 1, 7, '/log/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:49:15', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (23, 'sys:dic', '数据字典', 1, 0, '/sysdic/index', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (24, 'sys:dic:add', '添加数据字典', 23, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (25, 'sys:dic:update', '修改数据字典', 23, 2, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (26, 'sys:dic:del', '删除数据字典', 23, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (27, 'sys:dic:reload', '重载数据字典', 23, 4, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (28, 'sys:dic:status', '修改数据字典状态', 23, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (29, 'sys:monitor', '服务监控', 1, 4, '/monitor/index', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (30, 'sys:datasource', '数据源监控', 1, 5, '/druid', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (31, 'B1', '员工管理', 0, 1, null, 'layui-icon-user', 1, null, '2018-02-09 18:15:27', 1, '2018-10-31 09:25:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (32, 'B1_01', '员工管理', 31, 1, '/employee/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-08-21 14:59:32', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (33, 'B1_01_01', '添加员工', 32, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (34, 'B1_01_02', '修改员工', 32, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2019-09-19 15:41:49', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (35, 'B1_01_03', '删除员工', 32, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (36, 'B1_01_04', '修改员工状态', 32, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (37, 'B1_01_05', '显示金额', 32, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);

insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (1, '超级管理员', '2018-10-19 12:52:17', 1, 0, '2019-02-06 17:04:26', 1);
insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (2, '普通用户', '2018-10-26 01:15:48', 1, 0, '2019-09-25 22:54:15', 1);
insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (28, '测试角色', '2021-04-30 09:42:01', 1, 0, '2021-04-30 09:42:01', 1);

insert into t_role_menu (dbid, role_id, menu_id) values (4592, 28, 55);
insert into t_role_menu (dbid, role_id, menu_id) values (4593, 28, 56);
insert into t_role_menu (dbid, role_id, menu_id) values (4594, 28, 58);
insert into t_role_menu (dbid, role_id, menu_id) values (4595, 28, 62);
insert into t_role_menu (dbid, role_id, menu_id) values (4596, 28, 63);
insert into t_role_menu (dbid, role_id, menu_id) values (4597, 28, 67);
insert into t_role_menu (dbid, role_id, menu_id) values (4598, 28, 68);
insert into t_role_menu (dbid, role_id, menu_id) values (4599, 28, 18);
insert into t_role_menu (dbid, role_id, menu_id) values (4600, 28, 19);
insert into t_role_menu (dbid, role_id, menu_id) values (4601, 28, 20);
insert into t_role_menu (dbid, role_id, menu_id) values (4602, 28, 21);
insert into t_role_menu (dbid, role_id, menu_id) values (4603, 28, 22);
insert into t_role_menu (dbid, role_id, menu_id) values (4604, 28, 23);
insert into t_role_menu (dbid, role_id, menu_id) values (4605, 28, 24);
insert into t_role_menu (dbid, role_id, menu_id) values (4606, 28, 29);
insert into t_role_menu (dbid, role_id, menu_id) values (4607, 28, 30);
insert into t_role_menu (dbid, role_id, menu_id) values (4608, 28, 31);
insert into t_role_menu (dbid, role_id, menu_id) values (4609, 28, 32);
insert into t_role_menu (dbid, role_id, menu_id) values (4610, 28, 33);
insert into t_role_menu (dbid, role_id, menu_id) values (4611, 28, 25);
insert into t_role_menu (dbid, role_id, menu_id) values (4612, 28, 26);
insert into t_role_menu (dbid, role_id, menu_id) values (4613, 28, 27);
insert into t_role_menu (dbid, role_id, menu_id) values (4614, 28, 28);
insert into t_role_menu (dbid, role_id, menu_id) values (4615, 28, 48);
insert into t_role_menu (dbid, role_id, menu_id) values (4616, 28, 54);
insert into t_role_menu (dbid, role_id, menu_id) values (4752, 1, 18);
insert into t_role_menu (dbid, role_id, menu_id) values (4753, 1, 19);
insert into t_role_menu (dbid, role_id, menu_id) values (4754, 1, 20);
insert into t_role_menu (dbid, role_id, menu_id) values (4755, 1, 21);
insert into t_role_menu (dbid, role_id, menu_id) values (4756, 1, 22);
insert into t_role_menu (dbid, role_id, menu_id) values (4757, 1, 23);
insert into t_role_menu (dbid, role_id, menu_id) values (4758, 1, 24);
insert into t_role_menu (dbid, role_id, menu_id) values (4759, 1, 1);
insert into t_role_menu (dbid, role_id, menu_id) values (4760, 1, 2);
insert into t_role_menu (dbid, role_id, menu_id) values (4761, 1, 3);
insert into t_role_menu (dbid, role_id, menu_id) values (4762, 1, 4);
insert into t_role_menu (dbid, role_id, menu_id) values (4763, 1, 5);
insert into t_role_menu (dbid, role_id, menu_id) values (4764, 1, 6);
insert into t_role_menu (dbid, role_id, menu_id) values (4765, 1, 7);
insert into t_role_menu (dbid, role_id, menu_id) values (4766, 1, 674);
insert into t_role_menu (dbid, role_id, menu_id) values (4767, 1, 675);
insert into t_role_menu (dbid, role_id, menu_id) values (4768, 1, 676);
insert into t_role_menu (dbid, role_id, menu_id) values (4769, 1, 11);
insert into t_role_menu (dbid, role_id, menu_id) values (4770, 1, 12);
insert into t_role_menu (dbid, role_id, menu_id) values (4771, 1, 13);
insert into t_role_menu (dbid, role_id, menu_id) values (4772, 1, 14);
insert into t_role_menu (dbid, role_id, menu_id) values (4773, 1, 15);
insert into t_role_menu (dbid, role_id, menu_id) values (4774, 1, 16);
insert into t_role_menu (dbid, role_id, menu_id) values (4775, 1, 677);
insert into t_role_menu (dbid, role_id, menu_id) values (4776, 1, 678);
insert into t_role_menu (dbid, role_id, menu_id) values (4777, 1, 8);
insert into t_role_menu (dbid, role_id, menu_id) values (4778, 1, 9);
insert into t_role_menu (dbid, role_id, menu_id) values (4779, 1, 10);
insert into t_role_menu (dbid, role_id, menu_id) values (4780, 1, 17);

insert into t_sys_dic (dbid, _key, _value, _profile, _desc) values (4, 'secKill', '0', null, null);

insert into t_user (dbid, user_name, password, nick_name, age, email, phone, status, gender, create_time, create_id, last_modify_time, last_modify_id, last_login_time, open_id, err_num, avatar) values (1, 'admin', '18526bf18b5fbe2f1c4f4a6745b25201', 'admin', 27, '1234567@qq.com', '15752175217', 0, 1, '2020-10-15 10:01:28', 1, '2020-10-23 19:05:58', 1, '2021-12-21 11:01:36', null, 0, null);
    
insert into t_user_role (dbid, user_id, role_id) values (1, 1, 1);
