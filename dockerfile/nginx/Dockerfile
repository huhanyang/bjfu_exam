FROM nginx

#拷贝配置文件
COPY nginx.conf /etc/nginx/

#添加腾讯云apt源
RUN mv /etc/apt/sources.list /etc/apt/sources.list.bak \
    && echo "deb http://mirrors.163.com/debian/ jessie main non-free contrib" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.163.com/debian/ jessie-proposed-updates main non-free contrib" >>/etc/apt/sources.list \
    && echo "deb-src http://mirrors.163.com/debian/ jessie main non-free contrib" >>/etc/apt/sources.list \
    && echo "deb-src http://mirrors.163.com/debian/ jessie-proposed-updates main non-free contrib" >>/etc/apt/sources.list \
    #&& apt-get update
