# Spring Boot Testcontainers project

A Spring Boot project to demonstrate the use of testcontainers to implement integration tests with external systems 

This README file will focus on the testcontainers features implementation. For more information about the other project features, please refer to the project template:  [spring-boot-template](https://github.com/andrecaiado/spring-boot-template).

# Contents

- [Getting Started](#getting-started)
    - [Main features](#main-features)
    - [Dependencies and requirements](#dependencies-and-requirements)
    - [Run the project](#run-the-project)
- [RabbitMQ configuration](#rabbitmq-configuration)

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

# RabbitMQ configuration

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
