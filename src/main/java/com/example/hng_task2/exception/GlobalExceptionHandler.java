package com.example.hng_task2.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = getAllValidationErrors(ex.getBindingResult());

        ExceptionResponse errorResponse = new ExceptionResponse(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ExceptionResponse> handleEmailExceptions(EntityNotFoundException ex) {
//        ValidationError errors = new ValidationError("email", ex.getMessage());
//        ExceptionResponse errorResponse = new ExceptionResponse(List.of(errors));
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
//    }

    private List<ValidationError> getAllValidationErrors(BindingResult bindingResult) {
        List<ValidationError> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.add(toValidationError(fieldError));
        }
        return errors;
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailExceptions(EmailAlreadyExistsException ex) {
        ValidationError errors = new ValidationError("email", ex.getMessage());
        ExceptionResponse errorResponse = new ExceptionResponse(List.of(errors));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

   @ExceptionHandler(BadCredentialsException.class)
   public ResponseEntity<AuthErrorResponse> handleAuthExceptions(BadCredentialsException ex) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    ex.getMessage() ,
                    Objects.equals(ex.getMessage(), "Registration unsuccessful") ? 400 : 401
            );
            return new ResponseEntity<>(
                    authErr,
                    Objects.equals(ex.getMessage(), "Registration unsuccessful") ?
                            HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED
            );
   }

   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    "Client error",
                    400
            );
            return new ResponseEntity<>(
                    authErr,
                    HttpStatus.BAD_REQUEST
            );
   }

   @ExceptionHandler(OrganisationNameEmptyException.class)
   public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(OrganisationNameEmptyException ex) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    "Client error",
                    400
            );
            return new ResponseEntity<>(
                    authErr,
                    HttpStatus.BAD_REQUEST
            );
   }

   @ExceptionHandler(UserAlreadyInOrganisationException.class)
   public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(UserAlreadyInOrganisationException ex) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    "Client error",
                    400
            );
            return new ResponseEntity<>(
                    authErr,
                    HttpStatus.BAD_REQUEST
            );
   }

   @ExceptionHandler(NotPermittedException.class)
   public ResponseEntity<AuthErrorResponse> handleNotPermittedExceptions(NotPermittedException ex) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    "Client error",
                    400
            );
            return new ResponseEntity<>(
                    authErr,
                    HttpStatus.BAD_REQUEST
            );
   }

   @ExceptionHandler(UsernameNotFoundException.class)
   public ResponseEntity<AuthErrorResponse> handleAuthExceptions(
           UsernameNotFoundException ex
   ) {
            var authErr = new AuthErrorResponse(
                    "Bad request",
                    ex.getMessage(),
                    Objects.equals(ex.getMessage(), "Registration unsuccessful") ? 400 : 401
            );
            return new ResponseEntity<>(
                    authErr,
                    Objects.equals(ex.getMessage(), "Registration unsuccessful") ?
                            HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED
            );
   }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ValidationError error = new ValidationError("request", "Malformed JSON request");
        ExceptionResponse errorResponse = new ExceptionResponse(List.of(error));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    private ValidationError toValidationError(FieldError fieldError) {
        return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }

//    private Map<String, String> toErrorMap(FieldError fieldError) {
//        Map<String, String> error = new HashMap<>();
//        error.put("field", fieldError.getField());
//        error.put("message", fieldError.getDefaultMessage());
//        return error;
//    }
}
