package com.example.springboottemplate;

import com.example.springboottemplate.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class SpringBootTemplateApplicationTests {

	@Test
	void contextLoads() {
	}
}
