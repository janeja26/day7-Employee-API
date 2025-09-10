package org.example.springbootdemo.repository;

import org.example.springbootdemo.domain.Employee;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Employee save(Employee employee) {
        long id = idGenerator.getAndIncrement();
        employee.setId(id);
        employees.put(id, employee);
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public List<Employee> findByGender(String gender) {
        return employees.values().stream()
                .filter(e -> e.getGender() != null && e.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    @Override
    public Employee update(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public void deleteById(Long id) {
        employees.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return employees.containsKey(id);
    }
}
