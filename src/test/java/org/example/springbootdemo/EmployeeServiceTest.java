package org.example.springbootdemo;

import org.example.springbootdemo.controller.EmployeeController;
import org.example.springbootdemo.domain.Employee;
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

        // 模拟Repository的save方法返回带ID的员工对象
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // 执行创建操作
        Employee result = employeeService.createEmployee(validEmployee);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(25, result.getAge());
    }
}
