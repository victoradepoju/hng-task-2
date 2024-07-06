package com.example.hng_task2.exception;

public record ValidationError(
        String field,
        String message
) {
}
