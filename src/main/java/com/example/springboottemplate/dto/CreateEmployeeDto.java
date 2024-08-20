package com.example.springboottemplate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateEmployeeDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String designation;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
}
