package com.example.springboottemplate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateEmployeeDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String designation;
    private String phoneNumber;
    private LocalDate joinedOn;
    private String address;
    private LocalDate dateOfBirth;
}
