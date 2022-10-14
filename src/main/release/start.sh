#!/bin/bash
env_args="-Xms8g -Xmx8g -XX:MaxDirectMemorySize=2048M -Dloader.path=./,./lib,./resources"
APP_NAME="$(find /home/demo/ -name 'demo*.jar')"
pid=$(ps -ef | grep ${APP_NAME} | grep -v grep | awk '{print $2}')
if [[ "$pid" != "" ]]; then
  echo "${APP_NAME} already startup!"
else
  nohup java $env_args -javaagent:${APP_NAME}='-pwd Cyeg4HM6gmg0nMEpkz7CwA==' -jar ${APP_NAME} > /dev/null 2>&1 &
fi