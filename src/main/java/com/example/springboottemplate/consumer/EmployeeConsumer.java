package com.example.springboottemplate.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConsumer {

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeEmployee(@Payload Message message) {
        System.out.println("Employee consumed: " + message.getPayload());
    }
}
