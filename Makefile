include .env
export

DOCKER_IMAGE_NAME:=artalexm/transferservice

clean:
	./mvnw clean

test:
	./mvnw clean package -Dmaven.test.skip
	@echo '=== BUILD DOCKER IMAGE ==='
	docker build \
		--build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
		--build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
		--build-arg HOSTGROUP=${HOSTGROUP} \
		--build-arg HOSTUSER=${HOSTUSER} \
		--tag ${DOCKER_IMAGE_NAME}:test \
		./webapp
	@echo '=== STAR PROJECT TESTS ==='
	./mvnw test

build:
	./mvnw clean package
	@echo '=== BUILD DOCKER IMAGE ==='
	docker build \
        --build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
        --build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
        --build-arg HOSTGROUP=${HOSTGROUP} \
        --build-arg HOSTUSER=${HOSTUSER} \
        --tag ${DOCKER_IMAGE_NAME}:latest \
		--tag ${DOCKER_IMAGE_NAME}:${WEBAPP_VERSION} \
		./webapp

test-only:
	./mvnw test

run:
	${JAVA_HOME}/bin/java -jar ./webapp/target/transferservice-webapp-${WEBAPP_VERSION}.jar
