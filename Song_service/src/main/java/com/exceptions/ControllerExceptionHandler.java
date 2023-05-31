package com.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> notValidSong(Exception e){
        return new ResponseEntity<String>("Song metadata missing validation error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> allExceptions(Exception e){
        e.printStackTrace();
        logger.warn(e.getMessage());
        return new ResponseEntity<String>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
