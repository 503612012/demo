#!/bin/bash
. ./path.sh
count=$(docker ps -a | grep demo | grep -v mysql | wc -l)
if [[ $count > 0 ]]; then
  status=$(docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep demo | grep -v mysql | awk '{print $2}')
  if [[ $status = Up* ]]; then
    echo "demo container already started."
  else
    docker start demo
  fi
else
  docker run -d --name=demo -v ${APP_HOME}/logs:/home/demo/logs -v /data/storage/demo:/data/storage -v ${APP_HOME}/license:${APP_HOME}/license -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 6004:45678 demo:@version@
fi
