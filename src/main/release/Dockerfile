FROM harbor.qqxki.com/base/ubuntu-java-base:1.0.0
MAINTAINER Oven <503612012@qq.com>
LABEL Description="fms powered by oven" Version="@version@"

# 应用文件
RUN mkdir -p /home/fms/logs
ADD lib /home/fms/lib
ADD resources /home/fms/resources
ADD fms-@jar.version@.jar /home/fms
ADD start.sh /home/fms
RUN chmod +x /home/fms/start.sh
ADD stop.sh /home/fms
RUN chmod +x /home/fms/stop.sh
ADD app.sh /home/fms
RUN chmod +x /home/fms/app.sh

# 个性化配置
ENV PS1='\[\e[32m\]\u\[\e[35m\]@\[\e[36m\]\h \[\e[33m\]\w $ \[\e[0m\]'
ENV TIME_STYLE='+%Y-%m-%d %H:%M:%S'
RUN echo "alias c='clear'" >> ~/.bashrc
RUN echo "alias l='ls -lahk'" >> ~/.bashrc
RUN echo "alias ll='ls -lahk'" >> ~/.bashrc
RUN echo "alias log='tail -f /home/fms/logs/fms.log'" >> ~/.bashrc

WORKDIR /home/fms/
CMD /home/fms/app.sh && tail -f /home/fms/logs/fms.log