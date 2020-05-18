# OVEN SPRINGBOOT DEOM

## 安装步骤
### 1. 下载代码
git clone https://github.com/503612012/demo.git
### 2. 初始化数据
mysql加载demo.sql文件
### 3. 编译代码
进入项目根目录执行：mvn clean compile package
> 其中pom.xml文件中有一个自定义的插件，作用是将打包后的jar里边的自己编写的代码删除，启动的时候使用jar包外边的classes文件夹里边的文件。  
> 这样做的好处是以后升级的时候不用替换整个jar包（相当远全量升级）,只需要替换classes中指定的class文件即可（增量升级）。  
> 若找不到该插件，可以联系我：QQ503612012  
> 也可直接移步[oven-maven-plugin](https://github.com/503612012/oven-maven-plugin)项目，下载后部署到本地maven仓库中
### 4. 启动工程
进入项目根目录执行：./start.sh
### 5. 停止工程
进入项目根目录执行：./stop.sh
## 打包后的目录结构说明：
- classes：自己开发的类文件
- lib：第三方依赖jar包
- resources：资源文件和配置文件
- demo-1.0.0.jar：用于启动项目的jar，很小，只有几十Kb
- start.sh：启动脚本
- stop.sh：停止脚本