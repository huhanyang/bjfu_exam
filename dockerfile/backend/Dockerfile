FROM ubuntu:18.04

COPY build.sh /usr/src/build.sh
COPY start.sh /usr/src/start.sh

COPY sources.list /etc/apt/sources.list
RUN apt-get update \
    && sh /usr/src/build.sh \
    && apt-get install -y openjdk-11-jre-headless \
    && apt-get install -y maven \
    && apt-get install -y git

COPY settings.xml /usr/share/maven/conf/settings.xml
RUN [ "sh","/usr/src/build.sh" ]

ENTRYPOINT ["/usr/src/start.sh"]
