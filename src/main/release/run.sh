if [[ ! -d "/home/demo/logs" ]]; then mkdir /home/demo/logs; fi
if [[ ! -d "/home/demo/files" ]]; then mkdir /home/demo/files; fi
docker run -d --name=demo -v /home/demo/logs:/home/demo/logs -p 45678:45678 demo:1.0.0