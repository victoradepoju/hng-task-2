package com.victor.hng_task2.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
