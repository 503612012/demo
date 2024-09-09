create table t_config
(
    dbid    int auto_increment primary key,
    `key`   varchar(255)  null,
    `value` varchar(1023) null,
    `desc`  varchar(1023) null comment '描述信息'
);

create table t_crontab
(
    dbid  int auto_increment primary key,
    _key  varchar(63)  null comment 'cron表达式key',
    _desc varchar(255) null comment '描述信息',
    cron  varchar(31)  null comment 'cron表达式'
);

create table t_employee
(
    dbid             int auto_increment comment '主键' primary key,
    `name`           varchar(31)   null comment '员工姓名',
    age              int           null comment '年龄',
    `status`         int           null comment '状态：0-正常、1-删除',
    gender           int           null comment '性别：0-女、1-男',
    address          varchar(1023) null comment '住址',
    contact          varchar(15)   null comment '联系方式',
    hour_salary      double        null comment '时薪',
    create_id        int           null comment '创建人id',
    create_time      varchar(31)   null comment '创建时间',
    last_modify_id   int           null comment '最后修改人id',
    last_modify_time varchar(31)   null comment '最后修改时间',
    constraint t_employee_dbid_uindex unique (dbid)
) comment '员工表';

create table t_log
(
    dbid           int auto_increment comment '主键' primary key,
    title          varchar(63)   null comment '标题',
    request        varchar(5000) null comment '内容',
    response       varchar(5000) null comment '请求返回',
    request_uri    varchar(127)  null comment '请求uri',
    request_method varchar(15)   null comment '请求方法',
    operator_id    int           null comment '操作人id',
    operator_name  varchar(31)   null comment '操作人姓名',
    operator_time  varchar(31)   null comment '操作时间',
    operator_ip    varchar(31)   null comment '操作人ip地址',
    constraint t_log_dbid_uindex unique (dbid)
) comment '日志表';

create table t_menu
(
    dbid             int auto_increment comment '菜单表' primary key,
    menu_code        varchar(63)  null comment '菜单编码',
    menu_name        varchar(63)  null comment '菜单名称',
    pid              int          null comment '父id',
    sort             int          null comment '排序值',
    url              varchar(511) null comment '链接',
    iconCls          varchar(63)  null comment '图标',
    type             int          null comment '类型：1-目录、2-按钮',
    create_id        int          null comment '创建人id',
    create_time      varchar(31)  null comment '创建时间',
    last_modify_id   int          null comment '最后修改人id',
    last_modify_time varchar(31)  null comment '最后修改时间',
    `status`         int          null comment '状态：0-正常、1-删除',
    constraint menu_code unique (menu_code)
) comment '菜单表';

create table t_request_log_template
(
    request_time   varchar(31)   null comment '请求时间',
    request_url    varchar(1023) null comment '请求地址',
    request_method varchar(31)   null comment '请求方法',
    request_ip     varchar(127)  null comment '请求者ip',
    request_param  text          null comment '请求参数',
    user_id        int           null comment '登录人id'
) comment '接口请求日志模板表';

create table t_role
(
    dbid             int auto_increment comment '主键' primary key,
    role_name        varchar(31) null comment '角色名称',
    create_time      varchar(31) null comment '创建时间',
    create_id        int         null comment '创建人id',
    `status`         int         null comment '状态：0-正常、1-删除',
    last_modify_time varchar(31) null comment '最后修改时间',
    last_modify_id   int         null comment '最后修改人id',
    constraint t_role_dbid_uindex unique (dbid)
) comment '角色表';

create table t_role_menu
(
    dbid    int auto_increment comment '主键' primary key,
    role_id int null comment '角色id',
    menu_id int null comment '菜单id',
    constraint t_role_menu_dbid_uindex unique (dbid)
) comment '角色-菜单关系表';

create table t_sys_dic
(
    dbid     int auto_increment primary key,
    _key     varchar(1024) null,
    _value   varchar(1024) null,
    _profile varchar(31)   null,
    _desc    varchar(1024) null comment '描述信息',
    _status  int default 0 not null comment '状态：0正常、1停用'
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
    `password`       varchar(63)  null comment '密码',
    nick_name        varchar(31)  null comment '昵称',
    age              int          null comment '年龄',
    email            varchar(63)  null comment '邮箱',
    phone            varchar(11)  null comment '手机号',
    `status`         int          null comment '状态：0-正常、1-删除',
    gender           int          null comment '性别：0-女、1-男',
    create_time      varchar(31)  null comment '创建时间',
    create_id        int          null comment '创建人id',
    last_modify_time varchar(31)  null comment '最后修改时间',
    last_modify_id   int          null comment '最后修改人id',
    last_login_time  varchar(31)  null comment '最后登录时间',
    err_num          int          null comment '密码错误次数',
    avatar           varchar(127) null comment '头像地址',
    config           varchar(511) null comment '个性化配置'
) comment '用户表';

create table t_user_role
(
    dbid    int auto_increment comment '主键' primary key,
    user_id int null comment '用户id',
    role_id int null comment '角色id',
    constraint t_user_role_dbid_uindex unique (dbid)
) comment '用户-角色关系表';

insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'server.port', '45678', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'server.servlet.context-path', '/', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.thymeleaf.cache', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.type', 'com.alibaba.druid.pool.DruidDataSource', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.initial-size', '1', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.max-active', '20', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.min-idle', '1', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.max-wait', '1000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.pool-prepared-statements', 'true', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.max-open-prepared-statements', '20', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.validation-query', 'select 1 from dual', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.validation-query-timeout', '50000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.test-on-borrow', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.test-on-return', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.test-while-idle', 'true', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.time-between-eviction-runs-millis', '60000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.min-evictable-idle-time-millis', '30000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.max-evictable-idle-time-millis', '60000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.remove-abandoned', 'true', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.remove-abandoned-timeout', '1800', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.connection-properties', 'druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.max-pool-prepared-statement-per-connection-size', '20', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.stat-view-servlet.reset-enable', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.datasource.druid.filters', 'stat,wall', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.database', '0', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.host', '127.0.0.1', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.port', '6379', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.password', '123456', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.timeout', '30000', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.pool.max-active', '20', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.jedis.pool.max-wait', '-1', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.jedis.pool.max-idle', '8', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.redis.pool.min-idle', '0', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'springfox.documentation.swagger.v2.use-model-v3', 'false', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.boot.admin.client.url', 'http://192.168.18.66:8001', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.boot.admin.client.username', 'admin', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.boot.admin.client.password', '123456', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.boot.admin.client.instance.prefer-ip', 'true', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'management.endpoints.web.base-path', '/actuator', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'management.endpoints.web.exposure.include', '*', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'management.endpoint.health.show-details', 'always', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'management.endpoint.logfile.external-file', '${log.home}/demo.log', null);
# insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'management.endpoint.shutdown.enabled', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'logging.config', 'classpath:logback-pro.xml', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.data.redis.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.data.jpa.repositories.enabled', 'false', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'spring.transaction.rollback-on-commit-failure', 'true', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'avatar.path', '/data/storage/avatar/', '头像保存地址');
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'log.home', '/home/demo/logs/', '日志保存路径');
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'rsa.public_key', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6h42Pq2dHuMOU8eZT2CjvMgY2eizvW61WApQqWYuZwZ3BGChFiUehy4vh2JpW8lEFyX8eigawuVVRn55zDtbs/74ctfs2tUnyEhLX+em3ug1wCTlV2Sm8bYiBgejkXlzvy6RKvVaYspczIi3+146Y5ltcQVQ15Z9Us1eg10OWSwIDAQAB', null);
insert into t_config (dbid, `key`, `value`, `desc`) values (null, 'rsa.private_key', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALqHjY+rZ0e4w5Tx5lPYKO8yBjZ6LO9brVYClCpZi5nBncEYKEWJR6HLi+HYmlbyUQXJfx6KBrC5VVGfnnMO1uz/vhy1+za1SfISEtf56be6DXAJOVXZKbxtiIGB6OReXO/LpEq9VpiylzMiLf7XjpjmW1xBVDXln1SzV6DXQ5ZLAgMBAAECgYAuqEN/oWc3KH87nXp4lIRAuKmXLN5ajPsBnamEIlvp/OVB7JH+OsWBMhHuTyjkAL7FjM5QTEQ6JlGyFhQjQZ3+wbi3zK5edgTuxmfaAygCQxuByfClhmSMXokqTV8EC3jGC0+I360Xeyxf9Yx4F76iTDbUOOhoWrUqUDo7X4D2aQJBAP3bSuxpgF7GAb6oVBeRNkohTasKUsMZP4oYndtbCf8PCX1AhEf/S3EbMHGLH9i/FuOuinl4u9AM3TaKVuN2R10CQQC8GrvXDyQhhdDie+PE1JJRFWT7BzbSoa1sRoIeH8QweTYm0wYTjTEMZA2HavRK9eRxcLx756NTT7TKnCp1ScHHAkEA8xeaKcXlvdvTM+YxkNfnqxk1LQMXuehlpjJoh/QoIG0f4ydzBRb+AXPN8g0OFBr5KlKsHCJ3h4WqGF5lUjCjqQJAU78rRjJtiadEOrGh1K43fnYiAUlS9TAaQAycPcEy3cn5fAuTtvyzi6q4A0RxAza6wG7K2/djVeI0wcgJdzKhXQJBANXcZs4k8SHTk/I8pBANYBLJ2CKbZtkgw2xE1Oj6q/fjZWLSToV6LrVUOemTfvWfbKmXVz0U865X+HNpPO256iU=', null);

insert into t_crontab (dbid, _key, _desc, cron) values (1, 'REQUEST_LOG_CRON', '定时任务-保存接口请求记录', '0 */1 * * * ?');
insert into t_crontab (dbid, _key, _desc, cron) values (2, 'LOG_CRON', '定时任务-保存操作日志', '0 */1 * * * ?');

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
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (31, 'employee', '员工管理', 0, 1, null, 'layui-icon-user', 1, null, '2018-02-09 18:15:27', 1, '2018-10-31 09:25:38', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (32, 'employee:employee', '员工管理', 31, 1, '/employee/index', '', 1, null, '2018-02-09 18:15:27', null, '2018-08-21 14:59:32', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (33, 'employee:employee:add', '添加员工', 32, 1, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (34, 'employee:employee:update', '修改员工', 32, 2, null, '', 2, null, '2018-02-09 18:15:27', 1, '2019-09-19 15:41:49', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (35, 'employee:employee:del', '删除员工', 32, 3, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (36, 'employee:employee:status', '修改员工状态', 32, 4, null, null, 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);
insert into t_menu (dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_id, last_modify_time, `status`) values (37, 'employee:employee:showmeoney', '显示金额', 32, 5, null, '', 2, null, '2018-02-09 18:15:27', null, '2018-02-09 18:15:27', 0);

insert into t_role (dbid, role_name, create_time, create_id, `status`, last_modify_time, last_modify_id) values (1, '超级管理员', '2018-10-19 12:52:17', 1, 0, '2019-02-06 17:04:26', 1);
insert into t_role (dbid, role_name, create_time, create_id, `status`, last_modify_time, last_modify_id) values (2, '普通用户', '2018-10-26 01:15:48', 1, 0, '2019-09-25 22:54:15', 1);
insert into t_role (dbid, role_name, create_time, create_id, `status`, last_modify_time, last_modify_id) values (3, '测试角色', '2021-04-30 09:42:01', 1, 0, '2021-04-30 09:42:01', 1);

insert into t_role_menu (dbid, role_id, menu_id) values (1, 1, 1);
insert into t_role_menu (dbid, role_id, menu_id) values (2, 1, 2);
insert into t_role_menu (dbid, role_id, menu_id) values (3, 1, 3);
insert into t_role_menu (dbid, role_id, menu_id) values (4, 1, 4);
insert into t_role_menu (dbid, role_id, menu_id) values (5, 1, 5);
insert into t_role_menu (dbid, role_id, menu_id) values (6, 1, 6);
insert into t_role_menu (dbid, role_id, menu_id) values (7, 1, 7);
insert into t_role_menu (dbid, role_id, menu_id) values (8, 1, 8);
insert into t_role_menu (dbid, role_id, menu_id) values (9, 1, 9);
insert into t_role_menu (dbid, role_id, menu_id) values (10, 1, 10);
insert into t_role_menu (dbid, role_id, menu_id) values (11, 1, 11);
insert into t_role_menu (dbid, role_id, menu_id) values (12, 1, 12);
insert into t_role_menu (dbid, role_id, menu_id) values (13, 1, 13);
insert into t_role_menu (dbid, role_id, menu_id) values (14, 1, 14);
insert into t_role_menu (dbid, role_id, menu_id) values (15, 1, 15);
insert into t_role_menu (dbid, role_id, menu_id) values (16, 1, 16);
insert into t_role_menu (dbid, role_id, menu_id) values (17, 1, 17);
insert into t_role_menu (dbid, role_id, menu_id) values (18, 1, 18);
insert into t_role_menu (dbid, role_id, menu_id) values (19, 1, 19);
insert into t_role_menu (dbid, role_id, menu_id) values (20, 1, 20);
insert into t_role_menu (dbid, role_id, menu_id) values (21, 1, 21);
insert into t_role_menu (dbid, role_id, menu_id) values (22, 1, 22);
insert into t_role_menu (dbid, role_id, menu_id) values (23, 1, 23);
insert into t_role_menu (dbid, role_id, menu_id) values (24, 1, 24);
insert into t_role_menu (dbid, role_id, menu_id) values (25, 1, 25);
insert into t_role_menu (dbid, role_id, menu_id) values (26, 1, 26);
insert into t_role_menu (dbid, role_id, menu_id) values (27, 1, 27);
insert into t_role_menu (dbid, role_id, menu_id) values (28, 1, 28);
insert into t_role_menu (dbid, role_id, menu_id) values (29, 1, 29);
insert into t_role_menu (dbid, role_id, menu_id) values (30, 1, 30);
insert into t_role_menu (dbid, role_id, menu_id) values (31, 1, 31);
insert into t_role_menu (dbid, role_id, menu_id) values (32, 1, 32);
insert into t_role_menu (dbid, role_id, menu_id) values (33, 1, 33);
insert into t_role_menu (dbid, role_id, menu_id) values (34, 1, 34);
insert into t_role_menu (dbid, role_id, menu_id) values (35, 1, 35);
insert into t_role_menu (dbid, role_id, menu_id) values (36, 1, 36);
insert into t_role_menu (dbid, role_id, menu_id) values (37, 1, 37);

insert into t_sys_dic (dbid, _key, _value, _profile, _desc) values (4, 'secKill', '0', null, null);

insert into t_user (dbid, user_name, `password`, nick_name, age, email, phone, `status`, gender, create_time, create_id, last_modify_time, last_modify_id, last_login_time, err_num, avatar) values (1, 'admin', '18526bf18b5fbe2f1c4f4a6745b25201', 'admin', 27, '1234567@qq.com', '15752175217', 0, 1, '2020-10-15 10:01:28', 1, '2020-10-23 19:05:58', 1, '2021-12-21 11:01:36', 0, null);
    
insert into t_user_role (dbid, user_id, role_id) values (1, 1, 1);