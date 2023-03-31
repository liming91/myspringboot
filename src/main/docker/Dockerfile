FROM openjdk:8-jdk-alpine
#COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
MAINTAINER Ranger wilton.icp@gmail.com
WORKDIR /liming
VOLUME /tmp
EXPOSE 8085

ADD myspringboot-1.0.0.jar  /liming/myspringboot.jar
ENTRYPOINT ["java", "-Xmx512m","-Xmx1024m", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-jar", "/liming/myspringboot.jar"]