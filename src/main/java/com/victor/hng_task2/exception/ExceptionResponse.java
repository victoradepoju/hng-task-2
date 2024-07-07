package com.victor.hng_task2.exception;

import java.util.List;

public record ExceptionResponse(
        List<ValidationError> errors
) {
}
