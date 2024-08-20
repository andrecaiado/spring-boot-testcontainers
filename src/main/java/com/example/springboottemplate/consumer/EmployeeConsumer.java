package com.example.springboottemplate.consumer;

import com.example.springboottemplate.dto.CreateEmployeeDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmployeeConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeEmployee(@Payload CreateEmployeeDto createEmployeeDto) {
        System.out.println("Message " + createEmployeeDto + "  " + LocalDateTime.now());
    }
}
