package com.example.firstproject.globalhandler.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class ApiResponse<T> {
    private HttpStatus status;
    private T data;
    private ApiError error;

    public ApiResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }

    public ApiResponse(ApiError error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }
}
