FROM adoptopenjdk:14-jdk-hotspot

RUN apt-get update && apt-get install -y iputils-ping telnet

#LABEL io.openshift.expose-services="8080:8080"
ENV VERTICLE_FILE simple-store.jar
# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles


EXPOSE 8080

RUN mkdir -p $VERTICLE_HOME && groupadd vertx && useradd -g vertx vertx && chown -R vertx $VERTICLE_HOME && chmod -R g+w $VERTICLE_HOME

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

USER vertx
    
# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE "]