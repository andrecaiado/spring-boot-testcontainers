package com.example.springboottemplate.consumer;

import com.example.springboottemplate.dto.CreateEmployeeDto;
import com.example.springboottemplate.service.EmployeeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmployeeConsumer {

    private final EmployeeService employeeService;

    public EmployeeConsumer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeEmployee(@Payload CreateEmployeeDto createEmployeeDto) {
        System.out.println("Message " + createEmployeeDto + "  " + LocalDateTime.now());
        employeeService.saveEmployee(createEmployeeDto);
    }
}
