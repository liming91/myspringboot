FROM openjdk:8-jre-alpine
MAINTAINER Ranger wilton.icp@gmail.com

#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#&& echo 'Asia/hangzhou' >/etc/timezone

WORKDIR /liming
VOLUME /tmp
EXPOSE 8085

#COPY bin/  ./bin
#RUN chmod +x ./bin/start.sh

ADD myspringboot-1.0.0.jar  /liming/myspringboot.jar
ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-jar", "/liming/myspringboot.jar"]