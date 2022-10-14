#!/bin/bash
if [[ ! -d "/home/demo/logs" ]]; then mkdir /home/demo/logs; fi
if [[ ! -d "/home/demo/data/files" ]]; then mkdir /home/demo/data/files; fi
docker run -d --name=demo -v /home/demo/logs:/home/demo/logs -v /home/demo/data/files:/home/demo/files -v /home/demo/license:/home/demo/license -v /usr/sbin/dmidecode:/usr/sbin/dmidecode -v /dev/mem:/dev/mem --privileged=true -p 45678:45678 demo:1.0.0