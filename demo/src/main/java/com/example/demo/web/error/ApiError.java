package com.example.demo.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

public class ApiError {
    public final Instant timestamp = Instant.now();
    public final int status;
    public final String error;
    public final String message;
    public final String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final Map<String, String> fieldErrors;

    public ApiError(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}
