default: compile build

compile:
	mvn clean install package

build:
	docker-compose build

test:
	mvn test

run:
	docker-compose up -d && docker-compose logs -f dominos-import

stop:
	docker-compose down