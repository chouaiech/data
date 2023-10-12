FROM eclipse-temurin:17-jdk

ENV VERTICLE_FILE dataset-similarities.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

RUN mkdir -p $VERTICLE_HOME && groupadd vertx && useradd -g vertx vertx && chown -R vertx $VERTICLE_HOME && chmod -R g+w $VERTICLE_HOME

RUN mkdir /fingerprints && chown -R vertx /fingerprints && chmod -R 777 /fingerprints

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

USER vertx

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE"]
