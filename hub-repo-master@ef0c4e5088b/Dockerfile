FROM openjdk:17-jdk

ENV VERTICLE_FILE hub-repo.jar
# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles
# Set the location of the logback file
ENV LOG_HOME $VERTICLE_HOME/logs
ENV LOG_FILENAME incognito.log
# Set the log level
ENV LOG_LEVEL_FILE INFO
ENV XDG_CONFIG_HOME $VERTICLE_HOME

# Set the location of the config file
ARG CONFIG_FILE=config.sample.json
ARG HTTP_CONFIG_FILE=http-client.env.sample.json

ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"

EXPOSE 8080
EXPOSE 8085
EXPOSE 5000
EXPOSE 5005

RUN mkdir -p $VERTICLE_HOME/logs && groupadd vertx && useradd -g vertx vertx && chown -R vertx $VERTICLE_HOME && chmod -R g+w $VERTICLE_HOME

# Create log folder
#RUN mkdir $LOG_HOME && chown -R vertx $LOG_HOME && chmod -R g+w $LOG_HOME

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/
#COPY conf/$HTTP_CONFIG_FILE $VERTICLE_HOME/conf/http-client.env.json
COPY conf/$CONFIG_FILE $VERTICLE_HOME/conf/config.json
#COPY misc/ $VERTICLE_HOME/misc/

USER vertx

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE"]
