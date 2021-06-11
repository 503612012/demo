#!/bin/bash
env_args="-Xms128M -Xmx128M -XX:NewRatio=2 -Xverify:none -Dloader.path=./,./classes,./lib"
APP_NAME="`find ./ -name 'demo*.jar'`"
pid=`ps -ef | grep ${APP_NAME} | grep -v grep | awk '{print $2}'`
if [[ "$pid" != "" ]]
then
    echo "${APP_NAME} already startup!"
else
    nohup java $env_args -jar ${APP_NAME} > /dev/null 2>&1 &
fi