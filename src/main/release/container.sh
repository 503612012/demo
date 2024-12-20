#!/bin/bash
. ./path.sh
count=$(docker ps -a | grep fms | grep -v mysql | wc -l)
if [[ $count > 0 ]]; then
  status=$(docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep fms | grep -v mysql | awk '{print $2}')
  if [[ $status = Up* ]]; then
    echo "fms container already started."
  else
    docker start fms
  fi
else
  docker run -d --name=fms -v ${APP_HOME}/logs:/home/fms/logs -v /data/storage/fms:/data/storage -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 6004:45678 fms:@version@
fi