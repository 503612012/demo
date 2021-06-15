# Base image
FROM centos:7
MAINTAINER Oven <503612012@qq.com>
LABEL Description="This image is used to run springboot with jdk8." Version="1.0"

RUN mkdir -p /usr/local/
ADD jdk-8u181-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME=/usr/local/jdk1.8.0_181
ENV JAVA_BIN=/usr/local/jdk1.8.0_181/bin
ENV PATH=$PATH:$JAVA_HOME/bin
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

RUN mkdir -p /data/docker/demo/
ADD classes /data/docker/demo/classes
ADD lib /data/docker/demo/lib
ADD resources /data/docker/demo/resources
ADD demo-1.0.0.jar /data/docker/demo
ADD start.sh /data/docker/demo
RUN chmod +x /data/docker/demo/start.sh
ADD stop.sh /data/docker/demo
RUN chmod +x /data/docker/demo/stop.sh
ADD app.sh /data/docker/demo
RUN chmod +x /data/docker/demo/app.sh
RUN yum -y install kde-l10n-Chinese telnet && yum clean all && localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
ENV LC_ALL "zh_CN.UTF-8"

CMD ["sh", "/data/docker/demo/app.sh"]