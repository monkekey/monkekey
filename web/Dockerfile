FROM yumi/ffmpeg
#FROM java:8-jre
#FROM fabric8/java-jboss-openjdk8-jdk:1.2.3
MAINTAINER tdz <teddyzhou@126.com>

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD ./target/yumi-butler-service.jar /app/
ENTRYPOINT exec java $JAVA_OPTS -jar /app/yumi-butler-service.jar

EXPOSE 9080
EXPOSE 5544
