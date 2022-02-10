# OVEN SPRINGBOOT DEMO
## 安装步骤
### 1. 下载代码
git clone https://github.com/503612012/demo.git
### 2. 初始化数据
mysql加载demo.sql文件
### 3. 编译代码
进入项目根目录执行：mvn clean package -Ppro/dev
> -Ppro生产环境，-Pdev开发环境
### 4. 启动工程
进入项目根目录执行：./start.sh
### 5. 停止工程
进入项目根目录执行：./stop.sh
## 打包后的文件说明：
| 文件             | 用处             |
|----------------|----------------|
| lib            | 第三方依赖jar包      |
| resources      | 资源文件、前端文件和配置文件 |
| app.sh         | 容器启动后执行的脚本     |
| build.sh       | 镜像构建脚本         |
| demo.sql       | 数据库脚本          |
| demo-1.0.0.jar | 项目源码           |
| Dockerfile     | 镜像构建配置文件       |
| mysql.sh       | 数据库容器启动脚本      |
| run.sh         | 应用容器启动脚本       |
| simsun.ttf     | 字体文件           |
| start.sh       | 启动脚本           |
| stop.sh        | 停止脚本           |