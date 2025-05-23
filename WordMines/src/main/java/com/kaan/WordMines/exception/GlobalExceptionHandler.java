package com.kaan.WordMines.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameTableException.class)
    public ResponseEntity<String> handleGameTableException (GameTableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage()) ;
    }


}
