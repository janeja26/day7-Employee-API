package org.example.springbootdemo.service;

import org.example.springbootdemo.domain.Employee;
import org.example.springbootdemo.dto.UpdateEmployeeRequest;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees(String gender, Integer page, Integer size);
    Employee updateEmployee(Long id, UpdateEmployeeRequest updateRequest);
    void deleteEmployee(Long id);
}
