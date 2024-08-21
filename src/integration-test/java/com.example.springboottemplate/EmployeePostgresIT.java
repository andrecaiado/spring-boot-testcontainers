package com.example.springboottemplate;

import com.example.springboottemplate.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EmployeePostgresIT {

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

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(30);
        employee.setDesignation("Software Engineer");
        employee.setPhoneNumber("1234567890");
        employee.setJoinedOn(LocalDate.now());
        employee.setAddress("123, Baker Street, London");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testCreateEmployee() {
        String postUrl = "/employee/v1/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("content-type", "application/json");
        HttpEntity<Employee> requestEntity = new HttpEntity<>(employee, headers);
        ResponseEntity<Employee> response = testRestTemplate.exchange(postUrl,
                HttpMethod.POST,
                requestEntity,
                Employee.class);

        Employee savedEmployee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee.getFirstName(), savedEmployee.getFirstName());
        assertEquals(employee.getLastName(), savedEmployee.getLastName());
        assertEquals(employee.getAge(), savedEmployee.getAge());
        assertEquals(employee.getDesignation(), savedEmployee.getDesignation());
        assertEquals(employee.getPhoneNumber(), savedEmployee.getPhoneNumber());
        assertEquals(employee.getJoinedOn(), savedEmployee.getJoinedOn());
        assertEquals(employee.getAddress(), savedEmployee.getAddress());
        assertEquals(employee.getDateOfBirth(), savedEmployee.getDateOfBirth());
    }
}
