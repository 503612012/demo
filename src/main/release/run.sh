#!/bin/bash
. ./path.sh
if [[ ! -d "${APP_HOME}/logs" ]]; then mkdir ${APP_HOME}/logs; fi
if [[ ! -d "${APP_HOME}/data/files" ]]; then mkdir ${APP_HOME}/data/files; fi
docker run -d --name=demo -v ${APP_HOME}/logs:/home/demo/logs -v ${APP_HOME}/data/files:${APP_HOME}/files -v ${APP_HOME}/license:${APP_HOME}/license -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 45678:45678 -p 8082:8082 -p 8083:8083 -p 1936:1936 demo:1.0.0