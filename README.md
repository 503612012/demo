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
- [ ] demo.sql脚本重新弄一份
- [ ] 预支薪资模块
- [ ] 网络卡的时候重复点击录入两份的现象
- [ ] 增加入账模块
- [ ] 进入录入工时页面后，若没有查看员工薪资权限，则提示不能录入
- [ ] 所有的删除改成更改删除状态
- [ ] 考虑增加定时任务删除多余数据
- [ ] 录入工时时，若没有查看薪资权限则不自动填充薪资