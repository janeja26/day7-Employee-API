package org.example.springbootdemo.dto;

public class UpdateEmployeeRequest {
    private Integer age;
    private Double salary;

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}
