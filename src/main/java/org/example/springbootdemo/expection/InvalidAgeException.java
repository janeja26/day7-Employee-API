package org.example.springbootdemo.expection;

public class InvalidAgeException extends RuntimeException{

    public InvalidAgeException() {
        super("30岁及以上员工薪资不能低于20000");}
    public InvalidAgeException(String message) {
        super(message);
    }
}
