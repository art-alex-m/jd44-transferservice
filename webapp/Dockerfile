FROM openjdk:17-alpine

LABEL maintainer=artalexm

ARG WEBAPP_VERSION
ARG DOCKER_IMAGE_EXPOSE

ADD target/transferservice-webapp-$WEBAPP_VERSION.jar app.jar

EXPOSE $DOCKER_IMAGE_EXPOSE

CMD ["java", "-jar", "app.jar"]
