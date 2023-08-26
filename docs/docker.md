查看docker启动完整命令 docker ps -a --no-truncs

进入容器内部 docker exec -it 服务名称 bash

Docker是一个开源的容器引擎，它有助于更快地交付应用。Docker可将应用程序和基础设施层隔离，并且能将基础设施当作程序一样进行管理。
**使用Docker可更快速地打包、测试以及部署应用程序，并可以缩短从编写到部署运行代码的周期。**

### docker命令

官方文档命令地址 https://docs.docker.com/reference/

docker search 关键字

docker run -d 后台运行 -p

- —ip:hostPort:containerPort
- —ip:containerPort
- —hostPort:containerPort
- —containerPort
- —net选项：指定网络模式，该选项有以下可选参数：
- --net=bridge默认选项，表示连接到默认的网桥。
- --net=host:容器使用宿主机的网络。
- --net=container:NAME-or-ID：告诉Docker让新建的容器使用已有容器的网络配置。
- --net=none：不配置该容器的网络，用户可自定义网络配置。

docker pull

docker images

docker rmi java (docker images -q)删除所有镜像

docker ps

docker stop 容器ID

docker kill 容器ID

docker start 容器ID

docker inspect 容器ID 查看容器所有信息

docker container logs 容器ID

docker top 容器ID

# 查看正在运行docker容器的启动命令

**1. 通过docker ps命令**

```cmd
docker ps -a --no-trunc | grep container_name   # 通过docker --no-trunc参数来详细展示容器运行命令
```

**2. 通过docker [inspect](https://so.csdn.net/so/search?q=inspect&spm=1001.2101.3001.7020)命令**

使用docker inspect，但是docker inspect打印的是json文件，需要挑选字段查看，容器的参数信息都能从json文件中解析出来。

```cmd
1，在容器外部，物理机上，可以用下面命令
docker inspect <container_name>   # 可以是container_name或者container_id
 
# 默认的输出信息很多，可以通过-f, --format格式化输出：
docker inspect --format='{{.NetworkSettings.Networks.bridge.IPAddress}}' <container_name>      # format是go语言的template，还有其他的用法   
 
 
2，如果在容器内部。可以用下面命令
ps -ef    # 其中1号进程就是command启动命令
```

**3. 通过runlike三方包**

通过runlike去查看一个容器的docker run启动参数

```cmd
# 安装runlike安装包
pip install runlike
 
# 运行命令
runlike -p <container_name>  # 后面可以是容器名和容器id，-p参数是显示自动换行
```

