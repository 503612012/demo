if [[ ! -d "/home/data/demo/logs" ]]; then mkdir /home/data/demo/logs; fi
if [[ ! -d "/home/data/demo/files" ]]; then mkdir /home/data/demo/files; fi
docker run -d --name=demo -v /home/data/demo/logs:/home/demo/logs -v /home/data/demo/files:/home/demo/files -p 45678:45678 demo:1.0.0