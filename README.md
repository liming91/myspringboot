
##### 部署方式
docker build -t myspringboot:1.0.0 .

docker run -itd --name myspringboot -p 8085:8085 myspringboot:1.0.0

docker ps -a
## 内置功能
1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
## 在线地址
http://43.142.146.167:8088


感谢JetBrains支持https://www.jetbrains.com


