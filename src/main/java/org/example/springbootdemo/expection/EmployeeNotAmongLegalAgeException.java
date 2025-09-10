package org.example.springbootdemo.expection;

public class EmployeeNotAmongLegalAgeException extends RuntimeException {

    public EmployeeNotAmongLegalAgeException() {
        super("员工年龄应该在18到65岁之间");
    }

    public EmployeeNotAmongLegalAgeException(String message) {
        super(message);
    }
}
