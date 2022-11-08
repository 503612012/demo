#!/bin/bash
. ./path.sh
if [[ ! -d "${root_path}/logs" ]]; then mkdir ${root_path}/logs; fi
if [[ ! -d "${root_path}/data/files" ]]; then mkdir ${root_path}/data/files; fi
docker run -d --name=demo -v ${root_path}/logs:/home/demo/logs -v ${root_path}/data/files:${root_path}/files -v ${root_path}/license:${root_path}/license -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 45678:45678 -p 8082:8082 -p 8083:8083 -p 1936:1936 demo:1.0.0