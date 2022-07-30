### 一、部署建议

> 建议将所有文件放到`/home/demo`目录下，否则需要修改相关文件中的配置。

### 二、文件清单

| 文件                         | 描述        |
|----------------------------|-----------|
| lib                        | 第三方依赖包    |
| resources                  | 资源目录      |
| app.sh                     | 容器启动后执行脚本 |
| backup.sh                  | 数据库备份脚本   |
| build.sh                   | 镜像构建脚本    |
| Dockerfile                 | 镜像构建文件    |
| jdk-8u181-linux-x64.tar.gz | jdk安装包    |
| license.lic                | 授权文件      |
| mysql.sh                   | 数据库安装脚本   |
| publicCerts.keystore       | 公钥        |
| run.sh                     | 容器启动脚本    |
| simsun.ttf                 | 字体文件      |
| start.sh                   | 应用启动脚本    |
| stop.sh                    | 应用停止脚本    |
| demo.sql                   | 数据库初始化脚本  |
| demo-1.0.0.jar             | 应用代码      |

### 三、配置修改

> 修改`application-pro.properties`文件中的数据源配置

### 四、数据库部署

```shell
./mysql.sh
```

### 五、应用镜像构建

```shell
./build.sh
```

### 六、启动应用容器

```shell
./run.sh
```