FROM ubuntu:18.04

COPY settings.xml /usr/src/conf/settings.xml
COPY start.sh /usr/src/start.sh
COPY sources.list /usr/src/conf/sources.list

#添加腾讯云apt源
ENTRYPOINT ["/usr/src/start.sh"]
