package com.example.firstproject.globalhandler;


import com.example.firstproject.globalhandler.utils.ApiError;
import com.example.firstproject.globalhandler.utils.customexceptions.ResourceNotFound;
import jakarta.el.PropertyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError convertExceptionToApiError(Exception e) {
        return ApiError.builder().status(HttpStatus.NOT_FOUND).message(e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(Exception e) {
        return new ResponseEntity<>(convertExceptionToApiError(e),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<?> handlePropertyNotFound(PropertyNotFoundException e) {
        ApiError error = convertExceptionToApiError(e);
        return new ResponseEntity<>(error,error.getStatus());
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiError> handleResouceNotFound(ResourceNotFound e) {
        ApiError error = convertExceptionToApiError(e);
        return new ResponseEntity<>(error,error.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleWrongArgument(MethodArgumentNotValidException e) {
        List<String> subErrors = e
                .getAllErrors()
                .stream()
                .map(
                        error1 -> error1.getDefaultMessage()
                ).toList();
        ApiError error = ApiError.builder().status(HttpStatus.BAD_REQUEST).message("Input Validation Failed").subErrors(subErrors).build();

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
