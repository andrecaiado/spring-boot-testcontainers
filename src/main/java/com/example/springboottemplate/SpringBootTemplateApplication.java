package com.example.springboottemplate;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class SpringBootTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTemplateApplication.class, args);
	}

}
