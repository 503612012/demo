#!/bin/bash
. ./path.sh
mysql_mount_dir="${APP_HOME}/data/mysql"
is_first_start=false
if [ ! -d $mysql_mount_dir ]; then
  is_first_start=true
else
  if [ "$(ls -A $mysql_mount_dir)" ]; then
    echo "$mysql_mount_dir is not Empty"
  else
    is_first_start=true
  fi
fi

flag=true
while [[ "$flag" == true ]]; do
  read -s -p "请输入root用户密码(退出请输Y)：" passwd
  if [[ "$passwd" == "y" || "$passwd" == "Y" ]]; then
    exit
  fi

  if [[ ${#passwd} -lt 6 ]]; then
    echo $'\n密码长度至少6位！'
    continue
  fi

  if [[ "$passwd" != *[a-z]* ]]; then
    echo $'\n密码至少包含一个字母！'
    continue
  fi

  if [[ "$passwd" != *[0-9]* ]]; then
    echo $'\n密码至少包含一个数字！'
    continue
  fi
  flag=fasle
done

echo -n "$passwd" | openssl rsautl -encrypt -pubin -inkey <(echo -e "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6h42Pq2dHuMOU8eZT2CjvMgY2eizvW61WApQqWYuZwZ3BGChFiUehy4vh2JpW8lEFyX8eigawuVVRn55zDtbs/74ctfs2tUnyEhLX+em3ug1wCTlV2Sm8bYiBgejkXlzvy6RKvVaYspczIi3+146Y5ltcQVQ15Z9Us1eg10OWSwIDAQAB\n-----END PUBLIC KEY-----") -out ./pwd

echo "mysql container starting..."
docker run -d -p 3309:3306 --name mysql-demo -u root -v ${APP_HOME}/demo/data/backup:/home/backup -v ${mysql_mount_dir}:/var/lib/mysql -v /etc/localtime:/etc/localtime --restart=always -e MYSQL_ROOT_PASSWORD=${passwd} mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --lower_case_table_names=1
echo "mysql container started"

if [ "$is_first_start" = false ]; then
  exit
fi

echo "database initialing..."
docker cp demo.sql mysql-demo:/home

i=0
str=''
while [ $i -le 50 ]; do
  printf "progress:[%-100s]%d%%\r" $str $(($i * 2))
  str+='##'
  let i++
  sleep 0.3
done
printf "\n"
echo "init database finished"

echo "begin to config database..."
docker exec -i mysql-demo bash <<EOP
mysql -uroot -p${passwd} <<EOF
set names utf8;
create database db_demo default character set utf8 collate utf8_general_ci;
use mysql;
create user demo identified by 'demo9876';
grant all on db_demo.* to 'demo'@'%';
flush privileges;
use db_demo;
source /home/demo.sql;
exit
EOF
exit
EOP
echo "config database finished"
rm -rf ./mysql.sh
rm -rf ./demo.sql
