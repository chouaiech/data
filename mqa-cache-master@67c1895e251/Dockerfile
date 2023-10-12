FROM eclipse-temurin:17-jdk

ENV VERTICLE_FILE cache.jar
# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles
# Set the location of the logback file
ENV LOG_HOME $VERTICLE_HOME/logs
ENV LOG_FILENAME incognito.log
# Set the log level
ENV LOG_LEVEL_FILE INFO

EXPOSE 8080

RUN mkdir -p $VERTICLE_HOME && mkdir -p $LOG_HOME && groupadd vertx && useradd -g vertx vertx && chown -R vertx $VERTICLE_HOME && chmod -R g+w $VERTICLE_HOME

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

USER vertx

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE"]
