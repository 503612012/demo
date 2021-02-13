#!/bin/bash

env_args="-Xms128m -Xmx128m -Dloader.path=./,./classes,./lib"
sleeptime=0
arglen=$#

get_pid(){
    pname="`find .. -name 'demo*.jar'`"
    pname=${pname:3}
    pid=`ps -ef | grep ${pname} | grep -v grep | awk '{print $2}'`
    echo "$pid"
}

startup(){
    pid=$(get_pid)
    if [[ "$pid" != "" ]]
    then
        echo "Demo already startup!"
    else
        jar_path=`find .. -name 'demo*.jar'`
        echo "jarfile=$jar_path"
        cmd="java $1 -jar $jar_path > ./demo.out < /dev/null &"
        echo "cmd: $cmd"
        java $1 -jar ${jar_path} > ./demo.out < /dev/null &
        show_log
    fi
}

shut_down(){
    pid=$(get_pid)
    if [[ "$pid" != "" ]]
    then
        kill -9 ${pid}
        echo "Demo is stop!"
    else
        echo "Demo already stop!"
    fi
}

show_log(){
    tail -f demo.out
}

show_help(){
    echo -e "\r\n\t欢迎使用Demo"
    echo -e "\r\nUsage: sh demo.sh start|stop|reload|status|log"
    exit
}

show_status(){
    pid=$(get_pid)
    if [[ "$pid" != "" ]]
    then
        echo "Demo is running with pid: $pid"
    else
        echo "Demo is stop!"
    fi
}

if [[ ${arglen} -eq 0 ]]
 then
    show_help
else
    if [[ "$2" != "" ]]
    then
        env_args="$2"
    fi
    case "$1" in
        "start")
            startup "$env_args"
            ;;
        "stop")
            shut_down
            ;;
        "reload")
            echo "reload"
            ;;
        "status")
            show_status
            ;;
        "log")
            show_log
            ;;
    esac
fi