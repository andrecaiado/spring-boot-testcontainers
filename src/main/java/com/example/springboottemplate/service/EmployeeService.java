package com.example.springboottemplate.service;

import com.example.springboottemplate.dto.CreateUpdateEmployeeDto;
import com.example.springboottemplate.dto.EmployeeDto;
import com.example.springboottemplate.entity.Employee;
import com.example.springboottemplate.exception.UserNotFoundException;
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

    public List<EmployeeDto> getAllEmployees(){
        List<Employee> employees = findAllEmployees();
        return employees.stream().map(this::mapEmployeeToEmployeeDto).toList();
    }

    private List<Employee> findAllEmployees(){
        return employeeRepository.findAll();
    }

    public EmployeeDto getEmployeeById(Integer id){
        return mapEmployeeToEmployeeDto(findEmployeeById(id));
    }

    private Employee findEmployeeById(Integer id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        log.info("Employee with id: {} doesn't exist", id);
        throw new UserNotFoundException("Employee with id: " + id + " doesn't exist");
    }

    public EmployeeDto saveEmployee (CreateUpdateEmployeeDto createUpdateEmployeeDto){
        Employee employee = mapCreateUpdateEmployeeDtoToEmployee(createUpdateEmployeeDto, new Employee());

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee with id: {} saved successfully", employee.getId());
        return mapEmployeeToEmployeeDto(savedEmployee);
    }

    private Employee mapCreateUpdateEmployeeDtoToEmployee(CreateUpdateEmployeeDto createUpdateEmployeeDto, Employee employee) {
        employee.setFirstName(createUpdateEmployeeDto.getFirstName());
        employee.setLastName(createUpdateEmployeeDto.getLastName());
        employee.setAge(createUpdateEmployeeDto.getAge());
        employee.setDesignation(createUpdateEmployeeDto.getDesignation());
        employee.setPhoneNumber(createUpdateEmployeeDto.getPhoneNumber());
        employee.setJoinedOn(createUpdateEmployeeDto.getJoinedOn());
        employee.setAddress(createUpdateEmployeeDto.getAddress());
        employee.setDateOfBirth(createUpdateEmployeeDto.getDateOfBirth());

        return employee;
    }

    private EmployeeDto mapEmployeeToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setAge(employee.getAge());
        employeeDto.setDesignation(employee.getDesignation());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setJoinedOn(employee.getJoinedOn());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setDateOfBirth(employee.getDateOfBirth());
        employeeDto.setCreatedAt(employee.getCreatedAt());
        employeeDto.setUpdatedAt(employee.getUpdatedAt());

        return employeeDto;
    }

    public EmployeeDto updateEmployee (Integer id, CreateUpdateEmployeeDto employeeDto) {
        Employee employeeById = findEmployeeById(id);

        mapCreateUpdateEmployeeDtoToEmployee(employeeDto, employeeById);

        employeeById.setCreatedAt(employeeById.getCreatedAt());
        employeeById.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employeeById);

        log.info("Employee with id: {} updated successfully", id);
        return mapEmployeeToEmployeeDto(updatedEmployee);
    }

    public void deleteEmployeeById (Integer id) {
        employeeRepository.deleteById(id);
    }

}
