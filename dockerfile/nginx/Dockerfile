#构造编译环境
FROM ubuntu:18.04
COPY sources.list /etc/apt/sources.list
RUN apt-get update \
    && apt-get install -y wget \
    && apt-get install -y git \
    && apt-get install -y nodejs \
    && apt-get install -y npm \
    && npm config set registry https://registry.npm.taobao.org \
    && npm install -g npm \
    && npm install -g n \
    && n stable \
    && PATH="$PATH" \
    && git clone https://github.com/DF11G/newexam.git /usr/src/bjfu_exam \
    && cd /usr/src/bjfu_exam \
    && npm install \
    && npm run build \

#生成运行环境
FROM nginx
COPY nginx.conf /etc/nginx/
COPY --from=0 /usr/src/bjfu_exam/build /usr/src/bjfu_exam