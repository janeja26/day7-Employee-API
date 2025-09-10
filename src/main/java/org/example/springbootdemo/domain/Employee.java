package org.example.springbootdemo.domain;

import java.io.Serializable;

public class Employee implements Serializable {
    private long id;
    private String name;
    private int age;
    private String gender;
    private double salary;

    //public Employee() {}

    public Employee() {
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
