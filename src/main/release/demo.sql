create table t_advance_salary
(
    dbid         int auto_increment primary key,
    employee_id  int           null,
    has_repay    int default 1 null comment '是否归还，0是、1否',
    advance_time varchar(31)   null,
    money        double        null,
    create_id    int           null,
    remark       varchar(1023) null
);

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
    dbid             int auto_increment comment '主键',
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

alter table t_employee
    add primary key (dbid);

create table t_finance
(
    dbid             int auto_increment primary key,
    worksite_id      int           null,
    create_id        int           null,
    create_time      varchar(31)   null,
    last_modify_id   int           null,
    last_modify_time varchar(31)   null,
    money            double        null,
    out_money        double        null,
    del_flag         int default 0 null comment '0正常，1删除',
    del_id           int           null,
    del_time         varchar(31)   null,
    finish_flag      int default 1 null comment '是否清账，0是，1否',
    finish_id        int           null,
    finish_time      int           null,
    remark           varchar(1023) null
);

create table t_fund
(
    dbid        int auto_increment primary key,
    fund_name   varchar(127)     null,
    status      int              null comment '状态，0-正常、1-删除',
    create_id   int              null,
    create_time varchar(31)      null,
    _order      int default 9999 null
);

create table t_fund_bill
(
    dbid      int auto_increment primary key,
    fund_id   int           null,
    data_date varchar(31)   null,
    money     decimal(8, 2) null,
    status    int default 0 null comment '0正常，1禁用'
);

create table t_log
(
    dbid          int auto_increment comment '主键',
    title         varchar(63) null comment '标题',
    content       text        null comment '内容',
    operator_id   int         null comment '操作人ID',
    operator_name varchar(31) null comment '操作人姓名',
    operator_time varchar(31) null comment '操作时间',
    operator_ip   varchar(31) null comment '操作人IP地址',
    constraint t_log_dbid_uindex unique (dbid)
) comment '日志表';

alter table t_log
    add primary key (dbid);

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

create table t_pay_record
(
    dbid             int auto_increment primary key,
    payer_id         int          null,
    employee_id      int          null,
    pay_date         varchar(31)  null,
    total_money      double       null comment '总薪资',
    total_hour       int          null comment '总工时',
    workhour_ids     varchar(255) null comment '工时ID列表',
    remark           text         null,
    has_modify_money int          null comment '是否修改了金额，1是，0否',
    change_money     double       null
) comment '发薪记录表';

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
    dbid             int auto_increment comment '主键',
    role_name        varchar(31) null comment '角色名称',
    create_time      varchar(31) null comment '创建时间',
    create_id        int         null comment '创建人ID',
    status           int         null comment '状态，0-正常、1-删除',
    last_modify_time varchar(31) null comment '最后修改时间',
    last_modify_id   int         null comment '最后修改人ID',
    constraint t_role_dbid_uindex unique (dbid)
) comment '角色表';

alter table t_role
    add primary key (dbid);

create table t_role_menu
(
    dbid    int auto_increment comment '主键',
    role_id int null comment '角色ID',
    menu_id int null comment '菜单ID',
    constraint t_role_menu_dbid_uindex unique (dbid)
) comment '角色-菜单关系表';

alter table t_role_menu
    add primary key (dbid);

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
    avatar           varchar(127) null
) comment '用户表';

create table t_user_role
(
    dbid    int auto_increment comment '主键',
    user_id int null comment '用户ID',
    role_id int null comment '角色ID',
    constraint t_user_role_dbid_uindex unique (dbid)
) comment '用户-角色关系表';

alter table t_user_role
    add primary key (dbid);

create table t_wechat_fund
(
    dbid      int auto_increment primary key,
    data_date varchar(31)   null,
    money     decimal(8, 2) null
);

create table t_workhour
(
    dbid        int auto_increment primary key,
    employee_id int           null comment '员工ID',
    worksite_id int           null comment '工地ID',
    work_date   varchar(31)   null comment '工时日期',
    work_hour   int           null comment '录入工时',
    hour_salary double        null comment '录入工时时的时薪',
    create_id   int           null,
    create_time varchar(31)   null,
    has_pay     int default 0 null comment '是否发薪，0否，1是',
    remark      text          null comment '备注'
);

create table t_worksite
(
    dbid             int auto_increment primary key,
    name             varchar(127) null,
    `desc`           text         null,
    status           int          null comment '状态，0-正常、1-删除',
    create_id        int          null,
    create_time      varchar(31)  null,
    last_modify_id   int          null,
    last_modify_time varchar(31)  null
);

insert into t_config (dbid, `key`, value, `desc`) values (1, 'server.port', '45678', null);
insert into t_config (dbid, `key`, value, `desc`) values (2, 'server.servlet.context-path', '/', null);
insert into t_config (dbid, `key`, value, `desc`) values (4, 'spring.thymeleaf.cache', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (5, 'spring.datasource.type', 'com.alibaba.druid.pool.DruidDataSource', null);
insert into t_config (dbid, `key`, value, `desc`) values (6, 'spring.datasource.druid.max-wait', '60000', null);
insert into t_config (dbid, `key`, value, `desc`) values (7, 'spring.datasource.druid.min-idle', '1', null);
insert into t_config (dbid, `key`, value, `desc`) values (8, 'spring.datasource.druid.max-active', '100', null);
insert into t_config (dbid, `key`, value, `desc`) values (9, 'spring.datasource.druid.initial-size', '100', null);
insert into t_config (dbid, `key`, value, `desc`) values (10, 'spring.redis.database', '0', null);
insert into t_config (dbid, `key`, value, `desc`) values (11, 'spring.redis.host', '127.0.0.1', null);
insert into t_config (dbid, `key`, value, `desc`) values (12, 'spring.redis.port', '6379', null);
insert into t_config (dbid, `key`, value, `desc`) values (13, 'spring.redis.password', '5217', null);
insert into t_config (dbid, `key`, value, `desc`) values (14, 'spring.redis.timeout', '30000', null);
insert into t_config (dbid, `key`, value, `desc`) values (15, 'spring.redis.pool.max-active', '20', null);
insert into t_config (dbid, `key`, value, `desc`) values (16, 'spring.redis.jedis.pool.max-wait', '-1', null);
insert into t_config (dbid, `key`, value, `desc`) values (17, 'spring.redis.jedis.pool.max-idle', '8', null);
insert into t_config (dbid, `key`, value, `desc`) values (18, 'spring.redis.pool.min-idle', '0', null);
insert into t_config (dbid, `key`, value, `desc`) values (19, 'logging.config', 'classpath:logback-pro.xml', null);
insert into t_config (dbid, `key`, value, `desc`) values (29, 'spring.data.redis.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (30, 'spring.data.jpa.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, value, `desc`) values (31, 'spring.transaction.rollback-on-commit-failure', 'true', null);
insert into t_config (dbid, `key`, value, `desc`) values (37, 'avatar.path', '/data/docker/images/avatar/', '头像保存地址');

insert into t_crontab (dbid, _key, _desc, cron) values (1, 'REQUEST_LOG_CRON', '每分钟将队列中的请求日志保存到数据库', '0 */1 * * * ?');

insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (1, 'A1', '系统设置', 0, 4, null, 'layui-icon-set', 1, null, '2018-02-09 18:15:17', 1, '2018-10-30 20:51:29', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (2, 'A1_01', '用户管理', 1, 1, '/user/index', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (3, 'A1_01_01', '添加用户', 2, 1, '', '', 2, null, '2018-02-09 18:15:27', 1, '2019-06-13 18:33:44', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (4, 'A1_01_02', '修改用户', 2, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:21', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (5, 'A1_01_03', '删除用户', 2, 3, null, '', 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:47:26', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (6, 'A1_01_04', '修改用户状态', 2, 4, null, null, 2, null, '2018-02-09 18:15:27', 1, '2018-10-30 15:43:24', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (7, 'A1_01_05', '设置用户角色', 2, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (8, 'A1_02', '菜单管理', 1, 3, '/menu/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:52:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (9, 'A1_02_01', '修改名称', 8, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (10, 'A1_02_02', '修改菜单状态', 8, 2, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (11, 'A1_03', '角色管理', 1, 2, '/role/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (12, 'A1_03_01', '添加角色', 11, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (13, 'A1_03_02', '修改角色', 11, 2, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (14, 'A1_03_03', '删除角色', 11, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (15, 'A1_03_04', '修改角色状态', 11, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (16, 'A1_03_05', '设置角色权限', 11, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (17, 'A1_04', '系统日志', 1, 4, '/log/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:49:15', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (18, 'B1', '员工管理', 0, 1, null, 'layui-icon-user', 1, null, '2018-02-09 18:15:27', 1, '2018-10-31 09:25:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (19, 'B1_01', '员工管理', 18, 1, '/employee/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-08-21 14:59:32', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (20, 'B1_01_01', '添加员工', 19, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (21, 'B1_01_02', '修改员工', 19, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2019-09-19 15:41:49', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (22, 'B1_01_03', '删除员工', 19, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (23, 'B1_01_04', '修改员工状态', 19, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (24, 'B1_01_05', '显示金额', 19, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (25, 'B1_02', '工时管理', 18, 3, '/workhour/index', '', 1, null, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:14', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (26, 'B1_02_01', '工时录入', 25, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (27, 'B1_02_02', '删除工时', 25, 2, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (28, 'B1_02_03', '显示金额', 25, 3, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (29, 'B1_03', '工地管理', 18, 2, '/worksite/index', '', 1, null, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:08', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (30, 'B1_03_01', '添加工地', 29, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (31, 'B1_03_02', '修改工地', 29, 2, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (32, 'B1_03_03', '删除工地', 29, 3, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (33, 'B1_03_04', '修改工地状态', 29, 4, null, null, 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (34, 'C1', '工资管理', 0, 2, null, 'layui-icon-rmb', 1, null, '2018-08-21 18:15:27', 1, '2019-09-22 14:13:54', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (35, 'C1_01', '薪资发放', 34, 1, '/pay/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (36, 'C1_01_01', '更改实发金额', 35, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (37, 'C1_02', '发薪记录', 34, 2, '/payRecord/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (38, 'C1_02_01', '显示金额', 37, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (39, 'C1_04', '财务管理', 34, 4, '/finance/index', '', 1, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (40, 'C1_05', '薪资统计', 34, 5, '/salary/index', '', 1, null, '2018-08-21 18:15:27', 1, '2019-09-26 16:39:37', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (41, 'C1_03', '预支薪资', 34, 3, '/advanceSalary/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (42, 'C1_03_01', '添加预支薪资', 41, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (43, 'C1_03_02', '删除预支薪资', 41, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (44, 'C1_03_03', '显示金额', 41, 1, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (45, 'C1_04_01', '财务登记', 39, 1, null, '', 2, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (46, 'C1_04_02', '显示金额', 39, 3, null, '', 2, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (47, 'C1_04_03', '删除财务登记', 39, 2, null, '', 2, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (48, 'B1_02_04', '显示总工时', 25, 4, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (49, 'C1_01_02', '查看薪资总额', 35, 2, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (50, 'C1_02_02', '显示总金额', 37, 2, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (51, 'C1_03_04', '显示预支总额', 41, 4, null, '', 2, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (52, 'C1_04_04', '显示总登记金额', 39, 4, null, '', 2, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (53, 'C1_04_05', '显示总支出金额', 39, 5, null, '', 2, null, '2018-08-21 18:15:27', 1, '2019-01-10 20:28:56', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (54, 'B1_04', '工时报表', 18, 4, '/workhourReport/index', '', 1, null, '2018-08-21 18:15:27', 1, '2018-11-01 14:45:08', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (55, 'D1', '基金管理', 0, 0, null, 'layui-icon-diamond', 1, null, '2018-02-09 18:15:27', 1, '2018-10-31 09:25:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (56, 'D1_01', '基金管理', 55, 1, '/fund/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (57, 'D1_02', '收益报表', 55, 3, '/fundBillCharts/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (58, 'D1_01_01', '添加基金', 56, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (59, 'D1_01_02', '修改基金', 56, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2019-09-19 15:41:49', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (60, 'D1_01_03', '删除基金', 56, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (61, 'D1_01_04', '修改基金状态', 56, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (62, 'D1_03', '收益管理', 55, 2, '/fundBill/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (63, 'D1_03_01', '录入收益', 62, 1, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (64, 'D1_03_02', '修改收益', 62, 2, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (65, 'D1_03_03', '删除收益', 62, 3, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (66, 'D1_01_05', '修改基金排序值', 56, 5, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (67, 'D1_04', '微信基金', 55, 4, '/wechatFund/index', '', 1, null, '2018-08-21 18:15:27', null, '2018-08-21 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (68, 'D1_04_01', '录入收益', 67, 1, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (670, 'G1_01', '用户管理', 7, 1, '/user/index', '', 1, null, '2018-02-09 18:15:27', 1, '2019-09-18 10:55:01', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (671, 'G1_02', '菜单管理', 7, 3, '/menu/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:52:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (672, 'G1_03', '角色管理', 7, 2, '/role/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (673, 'G1_04', '系统日志', 7, 4, '/log/index', '', 1, null, '2018-02-09 18:15:27', 1, '2018-10-30 20:49:15', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (674, 'A1_01_06', '强制退出', 2, 6, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (675, 'A1_01_07', '重置错误次数', 2, 7, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, status) values (676, 'A1_01_08', '修改头像', 2, 8, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);

insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (1, '超级管理员', '2018-10-19 12:52:17', 1, 0, '2019-02-06 17:04:26', 1);
insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (2, '普通用户', '2018-10-26 01:15:48', 1, 0, '2019-09-25 22:54:15', 1);
insert into t_role (dbid, role_name, create_time, create_id, status, last_modify_time, last_modify_id) values (3, '测试角色', '2021-04-30 09:42:01', 1, 0, '2021-04-30 09:42:01', 1);

insert into t_role_menu (dbid, role_id, menu_id) values (4380, 1, 55);
insert into t_role_menu (dbid, role_id, menu_id) values (4381, 1, 56);
insert into t_role_menu (dbid, role_id, menu_id) values (4382, 1, 58);
insert into t_role_menu (dbid, role_id, menu_id) values (4383, 1, 59);
insert into t_role_menu (dbid, role_id, menu_id) values (4384, 1, 60);
insert into t_role_menu (dbid, role_id, menu_id) values (4385, 1, 61);
insert into t_role_menu (dbid, role_id, menu_id) values (4386, 1, 66);
insert into t_role_menu (dbid, role_id, menu_id) values (4387, 1, 62);
insert into t_role_menu (dbid, role_id, menu_id) values (4388, 1, 63);
insert into t_role_menu (dbid, role_id, menu_id) values (4389, 1, 64);
insert into t_role_menu (dbid, role_id, menu_id) values (4390, 1, 65);
insert into t_role_menu (dbid, role_id, menu_id) values (4391, 1, 57);
insert into t_role_menu (dbid, role_id, menu_id) values (4392, 1, 67);
insert into t_role_menu (dbid, role_id, menu_id) values (4393, 1, 68);
insert into t_role_menu (dbid, role_id, menu_id) values (4394, 1, 18);
insert into t_role_menu (dbid, role_id, menu_id) values (4395, 1, 19);
insert into t_role_menu (dbid, role_id, menu_id) values (4396, 1, 20);
insert into t_role_menu (dbid, role_id, menu_id) values (4397, 1, 21);
insert into t_role_menu (dbid, role_id, menu_id) values (4398, 1, 22);
insert into t_role_menu (dbid, role_id, menu_id) values (4399, 1, 23);
insert into t_role_menu (dbid, role_id, menu_id) values (4400, 1, 24);
insert into t_role_menu (dbid, role_id, menu_id) values (4401, 1, 29);
insert into t_role_menu (dbid, role_id, menu_id) values (4402, 1, 30);
insert into t_role_menu (dbid, role_id, menu_id) values (4403, 1, 31);
insert into t_role_menu (dbid, role_id, menu_id) values (4404, 1, 32);
insert into t_role_menu (dbid, role_id, menu_id) values (4405, 1, 33);
insert into t_role_menu (dbid, role_id, menu_id) values (4406, 1, 25);
insert into t_role_menu (dbid, role_id, menu_id) values (4407, 1, 26);
insert into t_role_menu (dbid, role_id, menu_id) values (4408, 1, 27);
insert into t_role_menu (dbid, role_id, menu_id) values (4409, 1, 28);
insert into t_role_menu (dbid, role_id, menu_id) values (4410, 1, 48);
insert into t_role_menu (dbid, role_id, menu_id) values (4411, 1, 54);
insert into t_role_menu (dbid, role_id, menu_id) values (4412, 1, 34);
insert into t_role_menu (dbid, role_id, menu_id) values (4413, 1, 35);
insert into t_role_menu (dbid, role_id, menu_id) values (4414, 1, 36);
insert into t_role_menu (dbid, role_id, menu_id) values (4415, 1, 49);
insert into t_role_menu (dbid, role_id, menu_id) values (4416, 1, 37);
insert into t_role_menu (dbid, role_id, menu_id) values (4417, 1, 38);
insert into t_role_menu (dbid, role_id, menu_id) values (4418, 1, 50);
insert into t_role_menu (dbid, role_id, menu_id) values (4419, 1, 39);
insert into t_role_menu (dbid, role_id, menu_id) values (4420, 1, 45);
insert into t_role_menu (dbid, role_id, menu_id) values (4421, 1, 47);
insert into t_role_menu (dbid, role_id, menu_id) values (4422, 1, 46);
insert into t_role_menu (dbid, role_id, menu_id) values (4423, 1, 52);
insert into t_role_menu (dbid, role_id, menu_id) values (4424, 1, 53);
insert into t_role_menu (dbid, role_id, menu_id) values (4425, 1, 40);
insert into t_role_menu (dbid, role_id, menu_id) values (4426, 1, 1);
insert into t_role_menu (dbid, role_id, menu_id) values (4427, 1, 2);
insert into t_role_menu (dbid, role_id, menu_id) values (4428, 1, 3);
insert into t_role_menu (dbid, role_id, menu_id) values (4429, 1, 4);
insert into t_role_menu (dbid, role_id, menu_id) values (4430, 1, 5);
insert into t_role_menu (dbid, role_id, menu_id) values (4431, 1, 6);
insert into t_role_menu (dbid, role_id, menu_id) values (4432, 1, 7);
insert into t_role_menu (dbid, role_id, menu_id) values (4433, 1, 674);
insert into t_role_menu (dbid, role_id, menu_id) values (4434, 1, 675);
insert into t_role_menu (dbid, role_id, menu_id) values (4435, 1, 676);
insert into t_role_menu (dbid, role_id, menu_id) values (4436, 1, 11);
insert into t_role_menu (dbid, role_id, menu_id) values (4437, 1, 12);
insert into t_role_menu (dbid, role_id, menu_id) values (4438, 1, 13);
insert into t_role_menu (dbid, role_id, menu_id) values (4439, 1, 14);
insert into t_role_menu (dbid, role_id, menu_id) values (4440, 1, 15);
insert into t_role_menu (dbid, role_id, menu_id) values (4441, 1, 16);
insert into t_role_menu (dbid, role_id, menu_id) values (4442, 1, 8);
insert into t_role_menu (dbid, role_id, menu_id) values (4443, 1, 9);
insert into t_role_menu (dbid, role_id, menu_id) values (4444, 1, 10);
insert into t_role_menu (dbid, role_id, menu_id) values (4445, 1, 17);
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

insert into t_sys_dic (dbid, _key, _value, _profile, _desc) values (4, 'secKill', '0', null, null);

insert into t_user (dbid, user_name, password, nick_name, age, email, phone, status, gender, create_time, create_id, last_modify_time, last_modify_id, last_login_time, open_id, err_num, avatar) values (1, 'Oven', '96ba8efe664c1b11fa56a691e1094cc8', 'Oven', 18, '1234567@qq.com', '15752175217', 0, 1, '2018-10-19 12:52:17', 1, '2020-01-09 18:41:42', 1, '2021-06-02 11:44:18', null, 0, null);
insert into t_user (dbid, user_name, password, nick_name, age, email, phone, status, gender, create_time, create_id, last_modify_time, last_modify_id, last_login_time, open_id, err_num, avatar) values (2, 'admin', '18526bf18b5fbe2f1c4f4a6745b25201', 'admin', 27, '1234567@qq.com', '15752175217', 0, 1, '2020-10-15 10:01:28', 1, '2020-10-23 19:05:58', 1, null, null, 2, null);

insert into t_user_role (dbid, user_id, role_id) values (1, 1, 1);
insert into t_user_role (dbid, user_id, role_id) values (2, 13, 1);
