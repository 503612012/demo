#!/bin/bash
count=$(docker images | grep harbor.qqxki.com/self/fms | grep @version@ | wc -l)
if [[ $count > 0 ]]; then
  echo "harbor.qqxki.com/self/fms:@version@ already exist~"
fi
docker pull harbor.qqxki.com/self/fms:@version@
echo "pull harbor.qqxki.com/self/fms:@version@ image success!"