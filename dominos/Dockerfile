FROM openjdk:14

RUN mkdir -p /opt/mariopizza/import
WORKDIR /opt/mariopizza/import
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar dominos.jar" ]
EXPOSE 8080

COPY ./src/main/resources/application.properties /opt/mariopizza/import/application.properties
COPY ./target/dominos-1.0-SNAPSHOT.jar /opt/mariopizza/import/dominos.jar

RUN touch api.jar