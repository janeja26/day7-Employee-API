package org.example.springbootdemo.expection;

public class EmployeeNotAmongLegalAgeException extends RuntimeException {

     public EmployeeNotAmongLegalAgeException() {
      super("员工年龄必须在18-65岁之间");}

    public EmployeeNotAmongLegalAgeException(String message) {
        super(message);
    }
}
