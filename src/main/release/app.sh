#!/bin/bash
cd /home/demo
if [ ! -f /home/demo/logs/demo.log ]; then
  touch /home/demo/logs/demo.log
fi
./start.sh