
##### 部署方式
docker build -t myspringboot:1.0.0 .

docker run -itd --name myspringboot -p 8085:8085 myspringboot:1.0.0

docker ps -a
## 内置功能
1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  prometheus + grafana搭建监控
7.  多线程批量导出 excel
8.  对称加密AES算法
9.  redisLockUtil
## 在线地址
http://43.142.146.167:8088


## api接口地址
http://127.0.0.1:8088/swagger-ui.html

## 上传路径
1.确保在 Docker 容器中目标目录 /home/excel 存在。你可以在 Dockerfile 中创建这个目录：RUN mkdir -p /home/excel && chmod 777 /home/excel
2.使用 Docker 卷挂载：docker run --init -d --name myspringboot -p 8088:8088 -v /home/excel:/home/excel  myspringboot:1.0.0

