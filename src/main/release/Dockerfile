FROM harbor.qqxki.com/base/ubuntu-java-base:1.0.0
MAINTAINER Oven <503612012@qq.com>
LABEL Description="demo powered by oven" Version="@version@"

# 应用文件
RUN mkdir -p /home/demo/logs
ADD lib /home/demo/lib
ADD resources /home/demo/resources
ADD demo-@jar.version@.jar /home/demo
ADD start.sh /home/demo
RUN chmod +x /home/demo/start.sh
ADD stop.sh /home/demo
RUN chmod +x /home/demo/stop.sh
ADD app.sh /home/demo
RUN chmod +x /home/demo/app.sh

# 个性化配置
ENV PS1='\[\e[32m\]\u\[\e[35m\]@\[\e[36m\]\h \[\e[33m\]\w $ \[\e[0m\]'
ENV TIME_STYLE='+%Y-%m-%d %H:%M:%S'
RUN echo "alias c='clear'" >> ~/.bashrc
RUN echo "alias l='ls -lahk'" >> ~/.bashrc
RUN echo "alias ll='ls -lahk'" >> ~/.bashrc
RUN echo "alias log='tail -f /home/demo/logs/demo.log'" >> ~/.bashrc

WORKDIR /home/demo/
CMD /home/demo/app.sh && tail -f /home/demo/logs/demo.log