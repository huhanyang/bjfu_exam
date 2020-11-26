#!/bin/bash

aptSourceFile="/etc/apt/sources.list.bak"
mvnSourceFile="/usr/share/maven/conf/settings.xml.bak"

echo -e "----开始更换源并更新软件包----"
if [ ! -f "$aptSourceFile" ]; then
    mv /etc/apt/sources.list /etc/apt/sources.list.bak
    mv /usr/src/conf/sources.list /etc/apt/sources.list
fi
apt-get update
#下载编译环境并更换maven源
echo -e "----开始下载更新编译环境并更换maven源----"
apt install -y openjdk-11-jre-headless
apt-get install -y maven
apt-get install -y git
if [ ! -f "$mvnSourceFile" ]; then
    mv /usr/share/maven/conf/settings.xml /usr/share/maven/conf/settings.xml.bak
    mv /usr/src/conf/settings.xml /usr/share/maven/conf/settings.xml
fi
#下载并编译源代码
echo -e "----开始下载并编译源代码----"
rm -rf /usr/src/bjfu_exam
git clone https://github.com/tank59he/bjfu_exam.git /usr/src/bjfu_exam/
cd /usr/src/bjfu_exam/
mvn clean package -Pprod -U -Dmaven.test.skip=true
#启动java程序
echo -e "----开始启动java程序----"
cd /usr/src/bjfu_exam/target/
java -jar bjfu_exam.jar