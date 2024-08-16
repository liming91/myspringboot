FROM openjdk:8-jdk-alpine
#COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
MAINTAINER Ranger wilton.icp@gmail.com
WORKDIR /liming
VOLUME /tmp
EXPOSE 8085
ADD myspringboot-1.0.0.jar  /liming/myspringboot.jar
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories \
    && apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime  \
    && echo "Asia/Shanghai" > /etc/timezone && apk del tzdata \
    && apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ENTRYPOINT ["java", "-Xmx512m","-Xmx1024m", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap", "-jar", "/liming/myspringboot.jar"]