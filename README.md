# 部署

[TOC]

## 简介

默认系统为ubuntu，使用docker来管理启动应用容器，数据库使用MySQL。

应用容器包括Jenkins交付平台、docker镜像仓库、部署了前端的Nginx服务器、后端应用。

服务器外网IP:49.232.112.133

## 环境

### docker容器

安装docker

```shell
sudo apt-get install docker.io
```

修改docker镜像仓库

```shell
sudo vim /etc/docker/daemon.json
```

文件内插入地址

```json
{
    "registry-mirrors": [
    	"https://docker.mirrors.ustc.edu.cn",
        "http://hub-mirror.c.163.com",
        "https://registry.docker-cn.com"
    ]
}
```

### Jenkins交付

使用docker的Jenkins镜像来启动Jenkins服务器

[jenkins/jenkins - Docker Hub](https://hub.docker.com/r/jenkins/jenkins)

```shell
#拉取镜像
docker pull jenkins/jenkins
#创建目录
mkdir -m 777 /var/jenkins_home
#启动容器
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 -v /var/jenkins_home:/var/jenkins_home jenkins/jenkins 
#查看密码
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

更改Jenkins源

```bash
echo -n"" > /var/jenkins_home/hudson.model.UpdateCenter.xml

echo -n "<?xml version='1.1' encoding='UTF-8'?><sites><site><id>default</id><url>https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json</url></site></sites>" > /var/jenkins_home/hudson.model.UpdateCenter.xml
```

### docker仓库

使用docker的registry镜像来启动docker镜像存储仓库

[registry - Docker Hub](https://hub.docker.com/_/registry)

```shell
#拉取镜像
docker pull registry
#启动容器
docker run -d -p 5000:5000 --restart always --name registry registry:2
```

```
<mirror>
  <id>alimaven</id>
  <mirrorOf>*</mirrorOf>
  <name>aliyun maven</name>
  <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
</mirror>
```

## 启动

启动前端服务器

```shell
docker run -p 80:80 -d --name bjfu_exam_nginx --network host exam:nginx
```

启动后端服务器

```shell
docker run -p 8080:8080 -d --name bjfu_exam_java --network host exam:java
```

启动数据库mysql服务器

```shell
docker run -p 3306:3306 -d --name mysql8 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=exam  mysql:8
```

启动图片服务器minio

```shell
docker run -p 9000:9000 -d --name minio1 \
  -v /mnt/data:/data \
  -v /mnt/config:/root/.minio \
  minio/minio server /data
```

初次初始化数据库请进入mysql的exam库中执行以下语句中创建管理员用户

```sql
insert into exam_user
    (account, name, password, state, type)
    VALUES ("adminadmin", "管理员账号", "adminadmin", 1, 3);
```