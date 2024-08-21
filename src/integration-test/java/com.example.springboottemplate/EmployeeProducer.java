package com.example.springboottemplate;

import com.example.springboottemplate.dto.CreateUpdateEmployeeDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.employees.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.employees.create.routing-key}")
    private String createRoutingKey;

    @Value("${rabbitmq.employees.update.routing-key}")
    private String updateRoutingKey;

    public void sendMessageCreateEmployee(CreateUpdateEmployeeDto message) {
        rabbitTemplate.convertAndSend(
                exchangeName, createRoutingKey, message);
    }

    public void sendMessageUpdateEmployee(String message) {
        rabbitTemplate.convertAndSend(
                exchangeName, updateRoutingKey, message);
    }
}
