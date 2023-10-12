FROM nginx:alpine

RUN apk update && apk add --no-cache iputils busybox-extras && apk add --no-cache curl

ADD dist.tar.gz /usr/share/nginx/html/
COPY nginx.vh.default.conf /etc/nginx/conf.d/default.conf

COPY runtimeconfig.sh /

RUN chmod +x /runtimeconfig.sh
RUN chmod a+rw /usr/share/nginx/html/static/js
RUN chmod a+rw /usr/share/nginx/html/static/js/*

# The following steps are needed because of the OpenShift security constraints
# Create some temp folders for later permission granting
RUN mkdir /var/cache/nginx/uwsgi_temp
RUN mkdir /var/cache/nginx/client_temp
RUN mkdir /var/cache/nginx/proxy_temp
RUN mkdir /var/cache/nginx/fastcgi_temp
RUN mkdir /var/cache/nginx/scgi_temp
# support running as arbitrary user which belogs to the root group
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx /var/cache/nginx/client_temp

EXPOSE 8080

CMD [ "/runtimeconfig.sh" ]