#!/bin/bash
APP_NAME="$(find /home/fms/ -name 'fms*.jar')"
pid=$(ps -ef | grep ${APP_NAME} | grep -v grep | awk '{print $2}')
if [[ "$pid" != "" ]]; then
  kill -9 ${pid}
else
  echo "${APP_NAME} already stop!"
fi