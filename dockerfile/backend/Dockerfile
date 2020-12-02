#构建编译环境
FROM ubuntu:18.04
COPY sources.list /etc/apt/sources.list
RUN apt-get update \
    && apt-get install -y openjdk-11-jre-headless \
    && apt-get install -y maven \
    && apt-get install -y git
COPY settings.xml /usr/share/maven/conf/settings.xml
RUN git clone https://github.com/tank59he/bjfu_exam.git /usr/src/bjfu_exam/ \
    && cd /usr/src/bjfu_exam/ \
    && mvn clean package -Pprod -U -Dmaven.test.skip=true \
    && mv /usr/src/bjfu_exam/target/bjfu_exam.jar /usr/src/ \
    && rm -rf /usr/src/bjfu_exam

#生成运行环境
FROM adoptopenjdk:11-jre-hotspot
COPY --from=0 /usr/src/bjfu_exam.jar /usr/src/bjfu_exam.jar
RUN rm -f /etc/localtime \
    && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
EXPOSE 8080
CMD ["java", "-jar", "/usr/src/bjfu_exam.jar"]
