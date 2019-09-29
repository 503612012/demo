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
- [ ] 数据库初始化脚本
- [ ] 一键对账
- [ ] 增加工时报表模块,在工时菜单哪里
- [ ] 已完结的工地，禁止发薪和删除录入财务

- 对账的话，统计录入工时，发放薪资，有没有漏发
- 统计工时发薪总额、统计工资录入中发薪总额、统计工资发放记录中发薪总额，对比三者有误差异
