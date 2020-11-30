#构造编译环境
FROM ubuntu:18.04
COPY sources.list /etc/apt/sources.list
RUN apt-get update \
    && apt-get install -y wget \
    && apt-get install -y git \
    && apt-get install -y wget \
    && apt-get install -y make \
    && wget https://npm.taobao.org/mirrors/node/v14.15.1/node-v14.15.1-linux-x64.tar.gz \
    && tar zxvf node-v14.15.1-linux-x64.tar.gz \
    && mv node-v14.15.1-linux-x64 /usr/sbin/nodejs \
    && echo 'export PATH=$PATH:/usr/local/bin' >> /etc/profile \
    && echo 'export NODE_HOME=/usr/sbin/nodejs' >> /etc/profile \
    && echo 'export PATH=$PATH:$NODE_HOME/bin' >> /etc/profile \
    && echo 'export NODE_PATH=$NODE_HOME/lib/node_modules' >> /etc/profile \
    && npm config set registry https://registry.npm.taobao.org \
    && npm install -g npm \
    && git clone https://github.com/tank59he/bjfu_exam_react.git /usr/src/bjfu_exam \
    && cd /usr/src/bjfu_exam \
    && npm install \
    && npm run build \

#生成运行环境
FROM nginx
COPY nginx.conf /etc/nginx/
COPY --from=0 /usr/src/bjfu_exam/build /usr/src/bjfu_exam
EXPOSE 80