include .env
export

DOCKER_IMAGE_TAG:=artalexm/transferservice:latest

image:
	./mvnw clean package
	@echo '=== BUILD DOCKER IMAGE ==='
	docker build \
		--build-arg WEBAPP_VERSION=${WEBAPP_VERSION} \
		--build-arg DOCKER_IMAGE_EXPOSE=${DOCKER_IMAGE_EXPOSE} \
		--tag ${DOCKER_IMAGE_TAG} ./webapp

clean:
	./mvnw clean

test:
	./mvnw test