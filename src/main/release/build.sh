#!/bin/bash
. ./path.sh
count=$(docker images | grep fms | grep @version@ | wc -l)
if [[ $count > 0 ]]; then
  docker rmi fms:@version@
fi
echo "begin build fms image..."
docker build -t fms:@version@ ./
echo "build fms image finish..."

flag=$(cat /var/spool/cron/root | grep fms | grep -v grep | awk '{print $6}')
if [ "$flag" == '' ]; then
  echo "begin set database backup schedule..."
  echo "0 */6 * * * ${APP_HOME}/backup" >>/var/spool/cron/root
  systemctl restart crond
  echo "set database backup schedule finish..."
else
  echo "the cron has already config~"
fi