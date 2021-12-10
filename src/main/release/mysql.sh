echo 'mysql container starting...'
docker run -d -p 3309:3306 --name mysql-demo -u root --restart=always -e MYSQL_ROOT_PASSWORD=1qaz@WSX3edc mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --lower_case_table_names=1
echo 'mysql container started'
echo 'demo.sql copying...'
docker cp demo.sql mysql-demo:/home

i=0
while (($i < 80)); do
  echo -e "=\c"
  let "i++"
  sleep 0.15
done
echo -e " demo.sql copy finished"

echo 'begin to config database...'
docker exec -i mysql-demo bash <<EOP
mysql -uroot -p1qaz@WSX3edc <<EOF
set names utf8;
create database db_demo default character set utf8 collate utf8_general_ci;
use mysql;
create user demo identified by 'demo';
grant all on db_demo.* to 'demo'@'%';
flush privileges;
use db_demo;
source /home/demo.sql;
exit
EOF
exit
EOP
echo 'config database finished'
