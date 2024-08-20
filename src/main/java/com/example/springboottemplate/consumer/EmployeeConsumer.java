package com.example.springboottemplate.consumer;

import com.example.springboottemplate.dto.CreateUpdateEmployeeDto;
import com.example.springboottemplate.service.EmployeeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class EmployeeConsumer {

    private final EmployeeService employeeService;

    public EmployeeConsumer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RabbitListener(queues = {"${rabbitmq.employees.create.queue}"})
    public void consumeCreateEmployee(@Payload CreateUpdateEmployeeDto createUpdateEmployeeDto) {
        System.out.println("Received message with payload: " + createUpdateEmployeeDto + " on " + LocalDateTime.now());
        
        employeeService.saveEmployee(createUpdateEmployeeDto);
    }

    @RabbitListener(queues = {"${rabbitmq.employees.update.queue}"})
    public void consumeUpdateEmployee(@Payload CreateUpdateEmployeeDto createUpdateEmployeeDto, @Headers Map<String, Object> headers) {
        Integer employeeId = Integer.valueOf(headers.get("employeeId").toString());

        System.out.println("Received message with employee ID: " + employeeId + " and payload: " + createUpdateEmployeeDto + " on " + LocalDateTime.now());

        employeeService.updateEmployee(employeeId, createUpdateEmployeeDto);
    }
}
