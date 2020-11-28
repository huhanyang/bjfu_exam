#!/bin/bash

#下载并编译源代码
echo "----开始下载源代码----"
git clone https://github.com/tank59he/bjfu_exam.git /usr/src/bjfu_exam/ || { echo "未找到项目代码目录"; exit 1; }

#编译源代码
echo "----开始编译源代码----"
cd /usr/src/bjfu_exam/ || { echo "未找到项目代码目录"; exit 1; }
mvn clean package -Pprod -U -Dmaven.test.skip=true
mv /usr/src/bjfu_exam/target/bjfu_exam.jar /usr/src/
rm -rf /usr/src/bjfu_exam