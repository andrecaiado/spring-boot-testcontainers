package com.example.springboottemplate;

import com.example.springboottemplate.dto.CreateUpdateEmployeeDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
public class EmployeeRabbitMQIT {

    @Autowired
    private EmployeeProducer producer;

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

    private static @NotNull CreateUpdateEmployeeDto getCreateUpdateEmployeeDto() {
        CreateUpdateEmployeeDto createUpdateEmployeeDto = new CreateUpdateEmployeeDto();
        createUpdateEmployeeDto.setFirstName("John");
        createUpdateEmployeeDto.setLastName("Doe");
        createUpdateEmployeeDto.setAge(30);
        createUpdateEmployeeDto.setDesignation("Software Engineer");
        createUpdateEmployeeDto.setPhoneNumber("1234567890");
        createUpdateEmployeeDto.setJoinedOn(LocalDate.of(2021, 1, 1));
        createUpdateEmployeeDto.setAddress("123, Baker Street, London");
        createUpdateEmployeeDto.setDateOfBirth(LocalDate.of(1979, 1, 1));

        return createUpdateEmployeeDto;
    }
}
