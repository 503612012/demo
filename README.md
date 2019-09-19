# OVEN SPRINGBOOT DEOM

## 安装步骤
#### 1. 下载代码
git clone https://github.com/503612012/demo.git
#### 2. 初始化数据
mysql加载demo.sql文件
#### 3. 编译代码
进入项目根目录执行：mvn clean compile package
#### 4. 启动工程
进入项目根目录执行：./demo.sh start
#### 5. 查看工程当前状态
进入项目根目录执行：./demo.sh status
#### 6. 查看日志
进入项目根目录执行：./demo.sh log
#### 7. 停止工程
进入项目根目录执行：./demo.sh stop

## TODO LIST
- [ ] 记住我功能
- [x] 加入一个定时任务
- [x] 加入一个图表
- [x] 加入一个队列
- [ ] 退出再登录后台报错