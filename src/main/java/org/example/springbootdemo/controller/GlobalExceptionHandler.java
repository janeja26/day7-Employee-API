package org.example.springbootdemo.controller;

import org.example.springbootdemo.expection.EmployeeNotAmongLegalAgeException;
import org.example.springbootdemo.expection.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(EmployeeNotAmongLegalAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeNotAmongLegalAgeException(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEmployeeNotFoundException(Exception e) {
        return e.getMessage();
    }

}
