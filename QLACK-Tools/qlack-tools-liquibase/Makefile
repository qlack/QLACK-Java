all: build push

build:
	docker build . -t eurodyn/qlack-tools-liquibase:3.8.9-2
	docker build . -t eurodyn/qlack-tools-liquibase:latest

push:
	docker login
	docker push eurodyn/qlack-tools-liquibase:3.8.9-2
	docker push eurodyn/qlack-tools-liquibase:latest
