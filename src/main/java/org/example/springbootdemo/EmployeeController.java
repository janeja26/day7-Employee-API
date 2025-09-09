package org.example.springbootdemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class EmployeeController {


    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        long id = idGenerator.getAndIncrement();
        Employee toSave = new Employee(id, employee.getName(), employee.getAge(),
                employee.getGender(), employee.getSalary());
        employees.put(id, toSave);
        return ResponseEntity.created(URI.create("/employees/" + id)).body(toSave);
    }


    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        Employee emp = employees.get(id);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(emp);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> listEmployees(
            @RequestParam(required = false) String gender) {
        List<Employee> list = employees.values().stream()
                .sorted(Comparator.comparingLong(Employee::getId))
                .collect(Collectors.toList());


        if (gender != null && !gender.isEmpty()) {
            list = list.stream()
                    .filter(e -> e.getGender() != null && e.getGender().equalsIgnoreCase(gender))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(list);
    }


    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id,
                                                   @RequestBody UpdateEmployeeRequest updateRequest) {
        Employee existing = employees.get(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (updateRequest.getAge() != null) {
            existing.setAge(updateRequest.getAge());
        }
        if (updateRequest.getSalary() != null) {
            existing.setSalary(updateRequest.getSalary());
        }
        return ResponseEntity.ok(existing);
    }




    public static class Employee {
        private long id;
        private String name;
        private int age;
        private String gender;
        private double salary;



        public Employee(long id, String name, int age, String gender, double salary) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.salary = salary;
        }

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public double getSalary() { return salary; }
        public void setSalary(double salary) { this.salary = salary; }
    }


    public static class UpdateEmployeeRequest {
        private Integer age;
        private Double salary;

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
    }

}
