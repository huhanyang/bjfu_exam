#构造编译环境
FROM node
RUN npm config set registry https://registry.npm.taobao.org \
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