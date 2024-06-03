# prometheus + grafana搭建监控

# **1.安装node-exporter监控系统信息**

编写 `node-exporter.yaml`​

```yaml
version: '3'
services:
  node_exporter:
    image: prom/node-exporter:latest
    container_name: node_exporter
    command:
      - '--path.rootfs=/host'
    pid: host
    restart: unless-stopped
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9100:9100
    volumes:
      - '/:/host:ro,rslave'
```

启动：

> docker-compose -f node-exporter.yaml up -d

这样我们就在9100端口暴露的监控指标数据，比如我们浏览器访问http://192.168.0.1:9100/metrics 就能看到采集的监控指标数据了（默认暴露在metrics路径下）

# **2.搭建prometheus，编辑prometheus docker-compose编排文件prometheus.yaml**

```yaml
version: '3'
services:
  prometheus:
    image: prom/prometheus:latest
    restart: always
    container_name: prometheus
    hostname: prometheus
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9090:9090
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/usr/share/prometheus/console_libraries' 
      - '--web.console.templates=/usr/share/prometheus/consoles' 
      - '--storage.tsdb.retention.time=7d'
      - '--web.external-url=prometheus'
    volumes:
      - /opt/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
      - /opt/promdata:/prometheus

```

参数介绍：

–config.file ：配置文件目录  
–storage.tsdb.path：指标数据存放目录  
–storage.tsdb.retention.time：保存7天内的指标数据  
–web.external-url：所有prometheus返回的url带上前缀prometheus，此项配置用来给nginx代理访问，可以不配置，配之后比如你的部署机器为192.168.1.169，那么访问prometheus控制台应为http://192.168.1.169:9090/prometheus  
/opt/prometheus/prometheus.yml(注意此处为宿主机目录)配置文件示例：

```yaml
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'node-exporter'
    scrape_interval: 8s
    static_configs:
    - targets: ['192.168.1.169:9100']
      labels:
        host: myhost01
    - job_name: 'oracle-192.168.1.175'
      static_configs:
        - targets: ['192.168.1.169:9161']
          labels:
            instance: oracle-192.168.1.175
    - job_name: 'redis-192.168.1.169'
      static_configs:
        - targets: ['192.168.1.169:9121']
          labels:
            instance: redis-192.168.1.169
  
    - job_name: 'cadvisor'
      static_configs:
        - targets: ['192.168.1.169:8080']
          labels:
            instance: cadvisor
```

job_name 说明prometheus需要采集哪些job以及从哪里采集，上面说到默认暴露在metrices路径下，prometheus默认也是去metrices路径下拉取指标，添加label prometheus就会在此处配置的exporter采集的所有监控指标数据里面添加一个标签为host：myhost01，当服务器需要有一些值来说明时，这个非常有用

> docker-compose -f prometheus.yaml up -d

 启动容器，浏览器访问http://192.168.0.1:9090/prometheus 就能看到prometheus的原生控制台了

# **3.搭建grafana，编辑grafana docker-compose编排文件grafana.yaml**

```yaml
version: '3'
services:
  grafana:
    image: grafana/grafana:latest
    restart: always
    container_name: grafana
    hostname: grafana
    environment:
      - TZ=Asia/Shanghai
    volumes:
    #  - /opt/grafana/defaults.ini:/etc/grafana/grafana.ini
      - /data/grafana:/var/lib/grafana
    ports:
      - 3000:3000
```

> docker-compose -f grafana.yaml up -d

 启动grafana，配置文件/opt/grafana/defaults.ini此处注释掉了，如果后续有一些基于配置文件的频繁改动，可以把容器中的配置文件/etc/grafana/grafana.ini ，docker cp 出来在放到宿主机/opt/grafana/defaults.ini此处，然后重启启动容器即可。

> docker cp 095006a89373:/etc/grafana/grafana.ini /opt/grafana/

浏览器访问http://192.168.1.169:3000 即可进去grafana登录页面，默认登陆账号密码为admin/admin，首次登陆会强制要求改密码

# **4.搭建oracledb_exporter，编辑oracledb_exporter docker-compose编排文件oracledb_exporter.yaml**

```yaml
version: '3'
services:
  oracledb_exporter:
    image: iamseth/oracledb_exporter
    container_name: oracledb_exporter
    restart: unless-stopped
    environment:
      - TZ=Asia/Shanghai
      - DATA_SOURCE_NAME=yb_etyy/yb_etyy@192.168.1.175:1521/ORCL
    ports:
      - 9161:9161
```

> docker-compose -f oracledb_exporter.yaml up -d

# 5 **.搭建redis_exporter，编辑redis_exporter docker-compose编排文件redis_exporter.yaml**

```yaml
docker run -d --name redis_exporter_01 -p 9121:9121 --restart=always oliver006/redis_exporter --redis.addr=redis://192.168.1.169:56379 --redis.password="ybzl123456."
```

```yaml
version: '3'
services:
  redis_exporter_01:
    image: oliver006/redis_exporter
    container_name: redis_exporter_01
    restart: unless-stopped
    command:
      - "-redis.addr=redis://192.168.1.69:56379"
      - "-redis.password=ybzl123456."
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9121:9121
```

# 6.cadvisor 安装 （docker监控）

```yaml
docker run -v /var/run:/var/run:rw  -v /sys:/sys:ro  -v /var/lib/docker:/var/lib/docker:ro -p 8080:8080  -d  --name cadvisor google/cadvisor
```

```yaml
version: '3'
services:
  cadvisor:
    image: google/cadvisor
    container_name: cadvisor
    ports:
      - 8080:8080
    volumes:
      - /:/rootfs:ro
      - /var/run:/var/run:rw
      - /sys:/sys:ro
      - /var/lib/docker/:/var/lib/docker:ro
```

# 7.配置**grafana ** 后台面板

常用面板ID

12633-------Linux主机详情

8919------------Node Exporter Dashboard 220413 ConsulManager自动同步版

11121-----------Oracledb监控-性能与表空间

1860------------Node Exporter Full

13555-----------OracleDB Monitoring - performance and table space stats

12884-----------Node Exporter

redis:11835------------redis

10585--------------docker

‍

# 统一docker-compose

```yaml
version: '3'
services:
  node_exporter:
    image: prom/node-exporter:latest
    container_name: node_exporter
    command:
      - '--path.rootfs=/host'
    pid: host
    restart: unless-stopped
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9100:9100
    volumes:
      - '/:/host:ro,rslave'
  oracledb_exporter:
    image: iamseth/oracledb_exporter
    container_name: oracledb_exporter
    restart: unless-stopped
    environment:
      - TZ=Asia/Shanghai
      - DATA_SOURCE_NAME=yb_etyy/yb_etyy@192.168.1.175:1521/ORCL
    ports:
      - 9161:9161
  redis_exporter_01:
    image: oliver006/redis_exporter
    container_name: redis_exporter_01
    restart: unless-stopped
    command:
      - "-redis.addr=redis://192.168.1.69:56379"
      - "-redis.password=ybzl123456."
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9121:9121
  prometheus:
    image: prom/prometheus:latest
    restart: always
    container_name: prometheus
    hostname: prometheus
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 9090:9090
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/usr/share/prometheus/console_libraries' 
      - '--web.console.templates=/usr/share/prometheus/consoles' 
      - '--storage.tsdb.retention.time=7d'
      - '--web.external-url=prometheus'
    volumes:
      - /opt/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
      - /opt/promdata:/prometheus
  grafana:
    image: grafana/grafana:latest
    restart: always
    container_name: grafana
    hostname: grafana
    environment:
      - TZ=Asia/Shanghai
    volumes:
      - /opt/grafana/defaults.ini:/etc/grafana/grafana.ini
      - /data/grafana:/var/lib/grafana
    ports:
      - 3000:3000
```

‍
