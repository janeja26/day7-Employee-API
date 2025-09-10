package org.example.springbootdemo.expection;

public class InactiveEmployeeUpdateException extends RuntimeException {
    public InactiveEmployeeUpdateException() {
    super("不能更新已离职员工信息");
    }

    public InactiveEmployeeUpdateException(String message) {
        super(message);
    }
}