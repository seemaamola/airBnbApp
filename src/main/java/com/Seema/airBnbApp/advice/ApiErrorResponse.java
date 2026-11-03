package com.Seema.airBnbApp.advice;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse<T> {

    private LocalDateTime timeStamp;
    private T data;
    private ApiError error;

    public ApiErrorResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiErrorResponse(T data) {
        this();
        this.data = data;
    }

    public ApiErrorResponse(ApiError error) {
        this();
        this.error = error;
    }
}