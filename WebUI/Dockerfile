FROM nginx:latest
MAINTAINER tdz <teddyzhou@126.com>

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#ADD index.html /usr/share/nginx/html/index2.html
ADD ./dist/ /usr/share/nginx/html/

EXPOSE 80 443


#FROM nginx
#RUN echo '<h1>Hello, Docker!</h1>' > /usr/share/nginx/html/index.html