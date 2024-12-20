#!/bin/bash
count=$(docker images | grep harbor.qqxki.com/self/fms | grep @version@ | wc -l)
if [[ $count > 0 ]]; then
  docker rmi harbor.qqxki.com/self/fms:@version@
fi
docker tag fms:@version@ harbor.qqxki.com/self/fms:@version@
docker push harbor.qqxki.com/self/fms:@version@
echo "push harbor.qqxki.com/self/fms:@version@ success!"