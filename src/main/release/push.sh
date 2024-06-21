#!/bin/bash
count=$(docker images | grep harbor.qqxki.com/self/demo | grep @version@ | wc -l)
if [[ $count > 0 ]]; then
  docker rmi harbor.qqxki.com/self/demo:@version@
fi
docker tag demo:@version@ harbor.qqxki.com/self/demo:@version@
docker push harbor.qqxki.com/self/demo:@version@
echo "push harbor.qqxki.com/self/demo:@version@ success!"
