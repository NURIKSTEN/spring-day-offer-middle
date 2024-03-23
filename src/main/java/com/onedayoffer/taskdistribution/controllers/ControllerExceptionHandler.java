package com.onedayoffer.taskdistribution.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice (annotations= RestController.class)
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException exception) {
        return new ResponseEntity<>("Entity not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


}
