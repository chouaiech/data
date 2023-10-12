FROM eclipse-temurin:17-jdk

ENV VERTICLE_FILE reporter.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles
ENV XDG_CONFIG_HOME $VERTICLE_HOME


EXPOSE 8080

RUN mkdir -p $VERTICLE_HOME && groupadd vertx && useradd -g vertx vertx && chown -R vertx $VERTICLE_HOME && chmod -R g+w $VERTICLE_HOME

COPY src/main/resources $VERTICLE_HOME/src/main/resources

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

USER vertx

# Launch the verticle
WORKDIR $VERTICLE_HOME

ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE"]
