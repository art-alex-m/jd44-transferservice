include .env
export

DOCKER_IMAGE_NAME:=artalexm/transferservice

clean:
	./mvnw clean

test-image:
	./mvnw clean package -Dmaven.test.skip
	@echo '=== BUILD DOCKER TEST IMAGE ==='
	docker build \
		--build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
		--build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
		--build-arg HOSTGROUP=${HOSTGROUP} \
		--build-arg HOSTUSER=${HOSTUSER} \
		--tag ${DOCKER_IMAGE_NAME}:test \
		./webapp

test-only:
	./mvnw test

test:
	$(MAKE) test-image
	@echo '=== STAR PROJECT TESTS ==='
	$(MAKE) test-only

build:
	$(MAKE) test-image
	./mvnw package
	@echo '=== BUILD RELEASE DOCKER IMAGE ==='
	docker build \
        --build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
        --build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
        --build-arg HOSTGROUP=${HOSTGROUP} \
        --build-arg HOSTUSER=${HOSTUSER} \
        --tag ${DOCKER_IMAGE_NAME}:latest \
		--tag ${DOCKER_IMAGE_NAME}:${WEBAPP_VERSION} \
		./webapp

run:
	${JAVA_HOME}/bin/java -jar ./webapp/target/transferservice-webapp-${WEBAPP_VERSION}.jar
