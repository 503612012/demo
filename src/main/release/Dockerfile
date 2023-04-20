FROM centos:7
MAINTAINER Oven <503612012@qq.com>
LABEL Description="demo powered by oven" Version="1.0"

RUN mkdir -p /usr/local/
ADD jdk-8u341-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME=/usr/local/jdk1.8.0_341
ENV JAVA_BIN=/usr/local/jdk1.8.0_341/bin
ENV PATH=$PATH:$JAVA_HOME/bin
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

ADD simsun.ttf /usr/local/jdk1.8.0_341/jre/lib/fonts

RUN mkdir -p /home/demo/logs
ADD lib /home/demo/lib
ADD resources /home/demo/resources
ADD demo-@version@.jar /home/demo
ADD start.sh /home/demo
RUN chmod +x /home/demo/start.sh
ADD stop.sh /home/demo
RUN chmod +x /home/demo/stop.sh
ADD app.sh /home/demo
RUN chmod +x /home/demo/app.sh

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
RUN yum -y install kde-l10n-Chinese telnet && yum clean all && localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
ENV LC_ALL "zh_CN.UTF-8"

WORKDIR /home/demo/
CMD /home/demo/app.sh && tail -f /home/demo/logs/demo.log
