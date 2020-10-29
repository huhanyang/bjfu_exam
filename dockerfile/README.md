# Docker部署与上传

[TOC]

## Docker镜像上传

### 新镜像构建

#### 构建命令

docker build -t bjfu_exam:latest .

### 新镜像上传

docker tag 镜像名称 huhanyang.tencentcloudcr.com/project/bjfu_exam:[tag]

docker push huhanyang.tencentcloudcr.com/project/bjfu_exam:[tag]

## 使用Docker镜像部署

docker run -itd --name bjfu_exam -p 80:80 bjfu_exam