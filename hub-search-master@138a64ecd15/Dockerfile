FROM eclipse-temurin:17-jdk

ENV VERTICLE_FILE search.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles
ENV SITEMAPS_HOME /usr/verticles/sitemaps

# Set the location of the config file
ARG CONFIG_FILE=config.sample.json

EXPOSE 8080 8081

RUN groupadd vertx && useradd -g vertx vertx

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/
COPY conf/elasticsearch $VERTICLE_HOME/conf/elasticsearch
COPY conf/vocabularies $VERTICLE_HOME/conf/vocabularies
COPY conf/$CONFIG_FILE $VERTICLE_HOME/conf/config.json

RUN mkdir $SITEMAPS_HOME
RUN chown -R vertx:vertx $VERTICLE_HOME 
RUN chmod -R a+rwx $VERTICLE_HOME

USER vertx

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar $VERTICLE_FILE"]
