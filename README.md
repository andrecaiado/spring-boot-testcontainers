# Spring Boot Testcontainers project

A Spring Boot project to demonstrate the use of testcontainers to implement integration tests with external systems 

This README file will focus on the testcontainers features implementation. For more information about the other project features, please refer to the project template:  [spring-boot-template](https://github.com/andrecaiado/spring-boot-template).

# Contents

- [Getting Started](#getting-started)
    - [Main features](#main-features)
    - [Dependencies and requirements](#dependencies-and-requirements)
    - [Run the project](#run-the-project)
- [System Under Test (SUT)](#system-under-test-sut)
    - [Tests](#tests)
        - [Test #1: RabbitMQ and Postgres](#test-1-rabbitmq-and-postgres)
        - [Test #2: Postgres](#test-2-postgres)
    - [Run the tests](#run-the-tests)
- [External services configuration](#external-services-configuration)
    - [RabbitMQ configuration](#rabbitmq-configuration)
    - [Postgres configuration](#postgres-configuration)
- [Testcontainers configuration](#testcontainers-configuration)
- [Tests implementation](#tests-implementation)

# Getting Started

This section provides an overview of the main features, necessary dependencies, and step-by-step instructions to help you get the application up and running quickly.

## Main features

The main features of this project are:
- Message consumer from a RabbitMQ queue
- Write the consumed messages to a Postgres database
- Integration tests with external services using Testcontainers
- Docker compose to launch external services (Postgres and RabbitMQ)

## Dependencies and requirements

The following dependency are required to implement this project features:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit-test</artifactId>
    <scope>test</scope>
</dependency>
```

To launch and run the external services (Postgres, Prometheus and Grafana) in Docker containers, the following requirements are needed:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

To run the project, the requirements are:

- [Java 17 (or higher)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

## Run the project

To run the project, follow the steps below.

```shell
mvn spring-boot:run
```

The application will be available at [http://localhost:8080](http://localhost:8080).

The external services (Postgres and RabbitMQ) configured in the [docker-compose.yaml](docker-compose.yaml) file will automatically be launched due to the `spring-boot-docker-compose` dependency.

In any case, the external services can also be launched manually by running on of the following command:

```shell
# Start all the external services
docker compose up -d
```

```shell
# Start a specific external service 
docker compose up -d <service-name>
# Replace <service-name> with the service you want to start (postgres or rabbitmq)
```

# System Under Test (SUT)

The System Under Test (SUT) is a simple Spring Boot application with the following use cases:

- Consume messages from a RabbitMQ queue and write them to a Postgres database.
- Exposes endpoints to execute CRUD operations on the database.

![sut.png](src%2Fmain%2Fresources%2Fsut.png)

The integration tests to be implemented will use Testcontainers to launch the external services (Postgres and RabbitMQ) and test the application's interaction with these services.

## Tests

The following tests will be implemented:

### Test #1: RabbitMQ and Postgres

The first test will verify the application's ability to consume messages from a RabbitMQ queue and write them to a Postgres database.

### Test #2: Postgres

The second test will verify the application's ability to execute CRUD operations on the database.

## Run the tests

To run the integration tests, execute the following command:

```shell
mvn test
```

# External services configuration

This section provides an overview of the configuration of the external services (Postgres and RabbitMQ).

## RabbitMQ configuration

The RabbitMQ service was added and configured in the [docker-compose.yaml](docker-compose.yaml). The service will automatically be launched when the project is started because of the `spring-boot-docker-compose` dependency.

On startup, 2 configuration files are mounted to the container:

- [rabbitmq.config](docker/rabbitmq/rabbitmq.config)
- [definitions.json](docker/rabbitmq/definitions.json)

The `rabbitmq.config` file is used to specify the location of the `definitions.json` file. 

The `definitions.json` file contains the RabbitMQ configuration settings, such as:

- Users and permissions
- Virtual hosts
- Exchanges
- Queues
- Bindings

The RabbitMQ management console is available at [http://localhost:15672](http://localhost:15672) with the default credentials:

- Username: guest
- Password: guest

The application connection to the RabbitMQ service is configured in the [application.yml](src/main/resources/application.yml) file:

## Postgres configuration

The Postgres service was added and configured in the [docker-compose.yaml](docker-compose.yaml). The service will automatically be launched when the project is started because of the `spring-boot-docker-compose` dependency.

# Testcontainers configuration

This section provides an overview of the Testcontainers configuration.

# Tests implementation

This section provides an overview of the tests implementation with Testcontainers.
