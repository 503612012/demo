mkdir /home/demo/logs
docker run -d --name=demo -v /home/demo/logs:/home/demo/logs -v -p 45678:45678 demo:1.0.0