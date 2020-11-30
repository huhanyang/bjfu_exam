FROM node

RUN npm config set registry https://registry.npm.taobao.org \
    && git clone https://github.com/tank59he/bjfu_exam_react.git /usr/src/bjfu_exam \
    && cd /usr/src/bjfu_exam \
    && npm install \
    && npm run build


FROM nginx
COPY --from=0 /usr/src/bjfu_exam/build /usr/src/bjfu_exam
COPY nginx.conf /etc/nginx/
EXPOSE 80