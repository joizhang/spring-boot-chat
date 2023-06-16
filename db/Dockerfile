FROM mysql/mysql-server:8.0.32

MAINTAINER joizhang(zxlwhom@outlook.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./chat_web.sql /docker-entrypoint-initdb.d
