#!/bin/bash
. ./path.sh
docker run -d --name=demo -v ${APP_HOME}/logs:/home/demo/logs -v /data/storage/demo:/data/storage -v ${APP_HOME}/license:${APP_HOME}/license -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 6004:45678 demo:1.0.0