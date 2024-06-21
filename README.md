## 一、部署建议

> 建议将所有文件放到`/home/demo`目录下，否则需要修改相关文件中的配置。

## 二、文件清单

| 文件                         | 描述        |
|----------------------------|-----------|
| __`lib`__                  | 第三方依赖包    |
| __`resources`__            | 资源目录      |
| app.sh                     | 容器启动后执行脚本 |
| backup                     | 数据库备份脚本   |
| build.sh                   | 镜像构建脚本    |
| container.sh               | 容器启动脚本    |
| Dockerfile                 | 镜像构建文件    |
| jdk-8u341-linux-x64.tar.gz | jdk安装包    |
| mysql.sh                   | 数据库安装脚本   |
| path.sh                    | 项目部署根路径   |
| push.sh                    | 推送镜像脚本    |
| nginx.tar.gz               | nginx     |
| simsun.ttf                 | 字体文件      |
| start.sh                   | 应用启动脚本    |
| stop.sh                    | 应用停止脚本    |
| demo.sql                   | 数据库初始化脚本  |
| demo-1.0.0.jar             | 应用代码      |

## 三、配置修改

#### 1) 修改配置文件

> 修改`application-pro.properties`文件中的数据源配置

#### 2) 修改配置文件

> 修改`demo.sql`文件中的redis配置

## 四、数据库部署

```shell
./mysql.sh
```

## 五、应用镜像构建

```shell
./build.sh
```

## 六、启动应用容器

```shell
./container.sh
```

## 七、开发环境搭建

> 1. 修改 [src/main/resources/application-dev.properties](./src/main/resources/application-dev.properties) 中数据源信息，或启动时指定环境变量：`db.url=192.168.18.66:3306;db.uname=root;db.pwd=root`
> 2. 修改 [src/main/resources/logback-dev.xml](./src/main/resources/logback-dev.xml) 中日志保存路径信息
> 3. 修改 [src/main/java/com/oven/demo/framework/config/DevEnvSet.java](./src/main/java/com/oven/demo/framework/config/DevEnvSet.java) 中相关配置项
> 4. 修改 [pom.xml](./pom.xml) 中指定profile