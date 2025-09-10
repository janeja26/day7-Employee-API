package org.example.springbootdemo;

import org.example.springbootdemo.controller.EmployeeController;
import org.example.springbootdemo.domain.Employee;
import org.example.springbootdemo.expection.InvalidAgeException;
import org.example.springbootdemo.repository.EmployeeRepository;
import org.example.springbootdemo.service.EmployeeService;
import org.example.springbootdemo.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    @Test
    public void testCreateEmployeeWithValidAge_Success() {

        Employee validEmployee = new Employee();
        validEmployee.setName("张三");
        validEmployee.setAge(25);
        validEmployee.setGender("男");
        validEmployee.setSalary(5000.0);


        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        Employee result = employeeService.createEmployee(validEmployee);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(25, result.getAge());
    }


    @Test
    public void testCreateEmployeeOver30WithLowSalary_ShouldThrowException() {
        // 30岁，薪资15000（不符合条件）
        Employee invalidEmployee = new Employee();
        invalidEmployee.setName("张三");
        invalidEmployee.setAge(30);
        invalidEmployee.setSalary(15000.0);

        InvalidAgeException exception = assertThrows(InvalidAgeException.class, () -> {
            employeeService.createEmployee(invalidEmployee);
        });
        assertEquals("30岁及以上员工薪资不能低于20000", exception.getMessage());
    }

    @Test
    public void testCreateEmployeeOver30WithHighSalary_Success() {
        Employee validEmployee = new Employee();
        validEmployee.setName("李四");
        validEmployee.setAge(35);
        validEmployee.setSalary(25000.0);

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Employee result = employeeService.createEmployee(validEmployee);
        assertTrue(result.isActive()); // 验证默认激活状态为true
    }

    // 测试3：30岁以下且薪资低于20000的员工可以被创建
    @Test
    public void testCreateEmployeeUnder30WithLowSalary_Success() {
        Employee validEmployee = new Employee();
        validEmployee.setName("王五");
        validEmployee.setAge(28);
        validEmployee.setSalary(18000.0);

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Employee result = employeeService.createEmployee(validEmployee);
        assertNotNull(result);
        assertTrue(result.isActive());
    }

}
