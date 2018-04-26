FROM openjdk:latest

ARG JAR_NAME=kindle-push-0.0.1-SNAPSHOT.jar
ENV APP_JAR=kindle-push-0.0.1-SNAPSHOT.jar

WORKDIR /kindle-push

COPY ./build/libs/${JAR_NAME} /kindle-push

EXPOSE 8080

RUN echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar $APP_JAR" > startup.sh

CMD ["sh", "startup.sh"]