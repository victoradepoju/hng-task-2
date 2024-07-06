package com.example.hng_task2.exception;

public record AuthErrorResponse (
        String status,
        String message,
        Integer statusCode
) {
}
