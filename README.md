
##### 打包
docker build -t myspringboot:1.0.0 .
docker run -itd --name myspringboot -p 8085:8085 myspringboot:1.0.0
docker ps -a


