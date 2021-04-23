# Base image
FROM centos:7
MAINTAINER Oven <503612012@qq.com>
LABEL Description="This image is used to run springboot with jdk8."  Version="1.0"

RUN mkdir -p /usr/local/
ADD jdk-8u181-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME=/usr/local/jdk1.8.0_181
ENV JAVA_BIN=/usr/local/jdk1.8.0_181/bin
ENV PATH=$PATH:$JAVA_HOME/bin
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

RUN mkdir -p /usr/local/demo
ADD classes /usr/local/demo/classes
ADD lib /usr/local/demo/lib
ADD resources /usr/local/demo/resources
ADD demo-1.0.0.jar /usr/local/demo
ADD start.sh /usr/local/demo
RUN chmod +x /usr/local/demo/start.sh
ADD stop.sh /usr/local/demo
RUN chmod +x /usr/local/demo/stop.sh

EXPOSE 80
WORKDIR /usr/local/demo
CMD ["sh","start.sh"]
