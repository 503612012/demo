#!/bin/bash
count=$(docker images | grep harbor.qqxki.com/self/demo | grep @version@ | wc -l)
if [[ $count > 0 ]]; then
  echo "harbor.qqxki.com/self/demo:@version@ already exist~"
fi
docker pull harbor.qqxki.com/self/demo:@version@
echo "pull harbor.qqxki.com/self/demo:@version@ image success!"