# Spring Boot Testcontainers project

A Spring Boot project to demonstrate the use of testcontainers to implement integration tests with external systems

This README file will focus on the testcontainers features implementation. For more information about the other project features, please refer to the project template:  [spring-boot-template](https://github.com/andrecaiado/spring-boot-template).

# Contents

- [Getting Started](#getting-started)
    - [Main features](#main-features)
    - [Dependencies and requirements](#dependencies-and-requirements)
    - [Run the project](#run-the-project)
    - [Run the tests](#run-the-tests)
- [System Under Test (SUT)](#system-under-test-sut)
- [External services configuration](#external-services-configuration)
    - [RabbitMQ configuration](#rabbitmq-configuration)
    - [Postgres configuration](#postgres-configuration)
- [Testcontainers configuration](#testcontainers-configuration)
- [Tests implementation](#tests-implementation)
    - [RabbitMQ tests](#rabbitmq-tests)
    - [Postgres tests](#postgres-tests)

# Getting Started

This section provides an overview of the main features, necessary dependencies, and step-by-step instructions to help you get the application up and running quickly.

## Main features

The main features of this project are:
- Message consumer from a RabbitMQ queue
- Write the consumed messages to a Postgres database
- Integration tests with external services using Testcontainers
- Docker compose to launch external services (Postgres and RabbitMQ)

## Dependencies and requirements

The following dependency are required to implement this project's main features:

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
<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>junit-jupiter</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-testcontainers</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>rabbitmq</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.testcontainers</groupId>
<artifactId>postgresql</artifactId>
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

To run the project, execute the following command:

```shell
mvn spring-boot:run
```

## Run the integration tests

To run the tests, execute the following command:

```shell
mvn failsafe:integration-test
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

The tests will cover the following scenarios:

- The application's ability to consume messages from a RabbitMQ queue and write them to a Postgres database.

- The application's ability to execute CRUD operations on the database.

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

The application connection to the RabbitMQ service and the RabbitMQ queues, exchanges, and bindings are configured in:

- The [application.yml](src/main/resources/application.yml) file for the main application.

- The [application-integration-test.yml](src/test/resources/application-integration-test.yml) file for the integration tests.

## Postgres configuration

The Postgres service was added and configured in the [docker-compose.yaml](docker-compose.yaml). The service will automatically be launched when the project is started because of the `spring-boot-docker-compose` dependency.

A datasource for the Postgres database is autoconfigured by Spring Boot due to:

- The `postgresql` dependency is added to the project and therefore is present on the classpath.

- The `spring-boot-docker-compose` dependency is added to the project, which, beside launching the external service, also creates the connection details for the datasource.

# Testcontainers configuration

The Testcontainers were configured at the tests level. 

Thefollowing annotations were used to configure the Testcontainers:


- The `@Testcontainers` annotation is used to enable the Testcontainers support in the test class.

- The `@ExtendWith(OutputCaptureExtension.class)` annotation is used to capture the output of the application's logs.

- The `@Container` annotation is used to define the Testcontainers that will be launched before the tests are executed.

- The `@ServiceConnection` discovers the type of container that is annotated and creates a ConnectionDetails bean that can be used to connect to the service.

Below is an example of the configuration of the RabbitMQ and Postgres containers that can be found in the [EmployeeRabbitMQIT.java](src/test/java/com/acaiado/employeerabbitmq/EmployeeRabbitMQIT.java) file:

```java
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
public class EmployeeRabbitMQIT {
    
    ...

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16.3-alpine"
    );

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(
            "rabbitmq:3.13.6-management-alpine"
    ).withCopyFileToContainer(
            MountableFile.forClasspathResource("rabbitmq/rabbitmq.conf"),
            "/etc/rabbitmq/rabbitmq.conf"
    ).withCopyFileToContainer(
            MountableFile.forClasspathResource("rabbitmq/definitions.json"),
            "/etc/rabbitmq/definitions.json"
    );
    
    ...
}
```



# Tests implementation

This section provides an overview of the tests implemented to validate the application's interaction with the external services (RabbitMQ and Postgres).

## RabbitMQ tests

To test the application's ability to consume messages from a RabbitMQ queue, the implemented test has the following steps:

1. A message is sent to the RabbitMQ queue.

2. The test waits for the message to be consumed by the application and verifies if specific log messages are printed to the console.

Below is an example of the test implementation that can be found in the [EmployeeRabbitMQIT.java](src/test/java/com/acaiado/employeerabbitmq/EmployeeRabbitMQIT.java) file:

```java
@Test
void testCreateEmployee(CapturedOutput output) {
    CreateUpdateEmployeeDto createUpdateEmployeeDto = getCreateUpdateEmployeeDto();

    producer.sendMessageCreateEmployee(createUpdateEmployeeDto);

    Pattern pattern = Pattern.compile("\\w*Received message with payload: CreateUpdateEmployeeDto\\(firstName=John, lastName=Doe, age=30, designation=Software Engineer, phoneNumber=1234567890, joinedOn=2021-01-01, address=123, Baker Street, London, dateOfBirth=1979-01-01\\) on \\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\w*");
    await().atMost(10, TimeUnit.SECONDS)
            .until(findPatternInOutput(output, pattern), is(true));

    Pattern pattern2 = Pattern.compile("\\w*Employee with id: \\d+ saved successfully\\w*");
    await().atMost(10, TimeUnit.SECONDS)
            .until(findPatternInOutput(output, pattern2), is(true));
}

private Callable<Boolean> findPatternInOutput(CapturedOutput output, Pattern pattern) {
    return () -> pattern.matcher(output.getOut()).find();
}
```

## Postgres tests

To test the application's ability to execute CRUD operations on the database, the implemented test has the following steps:

1. An employee is created in the database.
2. The test verifies the body of the response returned by the application when the employee is created.

The test implementation that can be found in the [EmployeePostgresIT.java](src/test/java/com/acaiado/employeerabbitmq/EmployeePostgresIT.java) file.
