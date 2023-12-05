FROM openjdk:17-alpine

LABEL maintainer=artalexm
WORKDIR /app

ARG HOSTGROUP
ARG HOSTUSER
ARG WEBAPP_VERSION
ARG DOCKER_IMAGE_EXPOSE

ENV SERVER_PORT=$DOCKER_IMAGE_EXPOSE

ADD target/transferservice-webapp-$WEBAPP_VERSION.jar webapp.jar

EXPOSE $DOCKER_IMAGE_EXPOSE

RUN addgroup -g $HOSTGROUP transferservice
RUN adduser -DHs /bin/bash -G transferservice -u $HOSTUSER azz

USER azz
CMD ["java", "-jar", "webapp.jar"]
