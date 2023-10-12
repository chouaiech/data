FROM python:3.9

RUN groupadd -r uwsgi && useradd -r -g uwsgi uwsgi
COPY requirements_prod.txt /
RUN pip install -r requirements_prod.txt

WORKDIR /application
COPY application /application
COPY start_server.sh /
COPY application_config.ini /
RUN chown -R uwsgi:uwsgi /application

USER uwsgi
EXPOSE 5000 9090 9191

CMD ["/start_server.sh", "DOCKER"]