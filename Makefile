default: compile build

compile:
	mvn clean install package

build:
	docker-compose build

test:
	mvn test

prepare-integration-test:
	mvn --batch-mode --update-snapshots package
	docker-compose build
	docker-compose up -d postgresql

integration-test:
	docker-compose up -d
	docker-compose logs -f dominos-import

run:
	make prepare-integration-test
	make integration-test

stop:
	docker-compose down