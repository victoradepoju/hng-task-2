package com.example.hng_task2.exception;

public class UserAlreadyInOrganisationException extends RuntimeException {
    public UserAlreadyInOrganisationException(String exp) {
        super(exp);
    }
}
