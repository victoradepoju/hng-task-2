package com.example.hng_task2.exception;

public class OrganisationNameEmptyException extends RuntimeException{
    public OrganisationNameEmptyException(String exp) {
        super(exp);
    }
}
