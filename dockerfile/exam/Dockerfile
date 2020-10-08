FROM openjdk:11

#添加腾讯云apt源
RUN mv /etc/apt/sources.list /etc/apt/sources.list.bak \
    && echo "deb http://mirrors.cloud.tencent.com/debian-security buster/updates main" >>/etc/apt/sources.list \
    && echo "deb http://mirrors.cloud.tencent.com/debian/ buster main non-free contrib" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.cloud.tencent.com/debian/ buster-updates main non-free contrib" >>/etc/apt/sources.list \
    && echo "deb http://mirrors.cloud.tencent.com/debian/ buster-backports main non-free contrib" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.cloud.tencent.com/debian-security buster/updates main" >>/etc/apt/sources.list \
    && echo "deb-src http://mirrors.cloud.tencent.com/debian/ buster main non-free contrib" >>/etc/apt/sources.list \
    && echo "deb-src http://mirrors.cloud.tencent.com/debian/ buster-updates main non-free contrib" >>/etc/apt/sources.list \
    && echo "deb-src http://mirrors.cloud.tencent.com/debian/ buster-backports main non-free contrib" >>/etc/apt/sources.list \
    && apt-get update

#下载编译环境并更换maven源
RUN sudo apt-get install -y maven \
    && sudo apt-get install -y git \
    && mv /usr/share/maven/conf/settings.xml /usr/share/maven/conf/settings.xml.bak
COPY settings.xml /usr/share/maven/conf/settings.xml

#运行时
ENTRYPOINT bash \
    && git clone https://github.com/tank59he/bjfu_exam.git /usr/src/bjfu_exam/ \
    && cd /usr/src/bjfu_exam/ \
    && mvn clean package -Pprod -U -Dmaven.test.skip=true \
    && cd /usr/src/bjfu_exam/target/ \
    && java -jar bjfu_exam.jar
