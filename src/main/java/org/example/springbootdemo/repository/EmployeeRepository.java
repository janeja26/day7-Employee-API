package org.example.springbootdemo.repository;

import org.example.springbootdemo.domain.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    List<Employee> findByGender(String gender);
    Employee update(Employee employee);
    void deleteById(Long id);
    boolean existsById(Long id);
}
