#!/bin/bash
cd /home/fms
if [ ! -f /home/fms/logs/fms.log ]; then
  touch /home/fms/logs/fms.log
fi
./start.sh