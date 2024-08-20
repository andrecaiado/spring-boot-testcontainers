package com.example.springboottemplate.service;

import com.example.springboottemplate.dto.CreateEmployeeDto;
import com.example.springboottemplate.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.springboottemplate.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        return null;
    }

    public Employee saveEmployee (CreateEmployeeDto createEmployeeDto){
        Employee employee = mapCreateEmployeeToEmployee(createEmployeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee with id: {} saved successfully", employee.getId());
        return savedEmployee;
    }

    public Employee mapCreateEmployeeToEmployee (CreateEmployeeDto createEmployeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(createEmployeeDto.getFirstName());
        employee.setLastName(createEmployeeDto.getLastName());
        employee.setAge(createEmployeeDto.getAge());
        employee.setDesignation(createEmployeeDto.getDesignation());
        employee.setPhoneNumber(createEmployeeDto.getPhoneNumber());
        employee.setJoinedOn(createEmployeeDto.getJoinedOn());
        employee.setAddress(createEmployeeDto.getAddress());
        employee.setDateOfBirth(createEmployeeDto.getDateOfBirth());
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        return employee;
    }

    public Employee updateEmployee (Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getId());
        employee.setCreatedAt(existingEmployee.get().getCreatedAt());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Employee with id: {} updated successfully", employee.getId());
        return updatedEmployee;
    }

    public void deleteEmployeeById (Integer id) {
        employeeRepository.deleteById(id);
    }

}
