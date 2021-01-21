# Docker构建与部署

[TOC]

## 镜像

### bjfu_exam_develop

开发环境，占用80端口，将node前端3000端口转发到/，java后端8080端口转发到/exam/，minio服务器9000端口转发到/exam-img/

### bjfu_exam_frontend

线上环境前端服务器，占用80端口，负责前端静态资源，反向代理后端8080端口、minio服务器9000端口。

### bjfu_exam_backend

线上环境后端服务器，占用8080端口。

## 新镜像构建

#### 开发环境

docker build -t bjfu_exam_develop:latest .

#### 线上环境-前端

docker build -t bjfu_exam_frontend:latest .

#### 线上环境-后端

docker build -t bjfu_exam_backend:latest .

## 镜像部署

#### 开发环境

docker run -itd --name bjfu_exam_develop -p 80:80 bjfu_exam_develop

#### 线上环境-前端服务器

docker run -p 80:80 -d --name bjfu_exam_frontend --network host bjfu_exam_frontend 

#### 线上环境-后端服务器

docker run -p 8080:8080 -d --name bjfu_exam_backend --network host bjfu_exam_backend 