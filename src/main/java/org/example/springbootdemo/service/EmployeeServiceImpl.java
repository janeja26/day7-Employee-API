package org.example.springbootdemo.service;

import org.example.springbootdemo.domain.Employee;
import org.example.springbootdemo.dto.UpdateEmployeeRequest;

import org.example.springbootdemo.expection.EmployeeNotAmongLegalAgeException;
import org.example.springbootdemo.expection.InactiveEmployeeUpdateException;
import org.example.springbootdemo.expection.InvalidAgeException;
import org.example.springbootdemo.expection.ResourceNotFoundException;
import org.example.springbootdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        int age = employee.getAge();
        if (age < 18 || age > 65) {
            throw new EmployeeNotAmongLegalAgeException("员工年龄必须在18-65岁之间");
        }

        if (employee.getAge() >= 30 && employee.getSalary() < 20000) {
            throw new InvalidAgeException("30岁及以上员工薪资不能低于20000");
        }
        employee.setActive(true);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    @Override
    public List<Employee> getAllEmployees(String gender, Integer page, Integer size) {
        List<Employee> employees = (gender != null && !gender.isEmpty())
                ? employeeRepository.findByGender(gender)
                : employeeRepository.findAll();

        // 按ID排序
        employees.sort(Comparator.comparingLong(Employee::getId));

        // 处理分页
        if (page != null && size != null) {
            if (page < 1 || size < 1) {
                throw new IllegalArgumentException("Page and size must be positive integers");
            }

            int startIndex = (page - 1) * size;
            if (startIndex >= employees.size()) {
                return Collections.emptyList();
            }

            int endIndex = Math.min(startIndex + size, employees.size());
            return employees.subList(startIndex, endIndex);
        }

        return employees;
    }

    @Override
    public Employee updateEmployee(Long id, UpdateEmployeeRequest updateRequest) {
        Employee existingEmployee = getEmployeeById(id);

        if (!existingEmployee.isActive()) {
            throw new InactiveEmployeeUpdateException("不能更新已离职员工信息");
        }
        if (updateRequest.getAge() != null) {
            int newAge = updateRequest.getAge();
            if (newAge < 18 || newAge > 65) {
                throw new EmployeeNotAmongLegalAgeException("员工年龄必须在18-65岁之间");
            }
            existingEmployee.setAge(newAge);
        }
        if (updateRequest.getSalary() != null) {
            existingEmployee.setSalary(updateRequest.getSalary());
        }

        return employeeRepository.update(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeById(id);
        // 删除操作仅设置active状态为false
        existingEmployee.setActive(false);
        employeeRepository.update(existingEmployee);
    }
}
