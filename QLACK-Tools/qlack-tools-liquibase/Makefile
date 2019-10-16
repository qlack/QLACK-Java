all: build push

build:
	docker build . -t eurodyn/qlack-tools-liquibase:3.7.0-1
	docker build . -t eurodyn/qlack-tools-liquibase:latest

push:
	docker login
	docker push eurodyn/qlack-tools-liquibase:3.7.0-1
	docker push eurodyn/qlack-tools-liquibase:latest
