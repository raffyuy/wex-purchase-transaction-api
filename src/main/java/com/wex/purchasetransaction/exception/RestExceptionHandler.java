package com.wex.purchasetransaction.exception;

import org.h2.jdbc.JdbcSQLDataException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConversionRateNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ConversionRateNotFoundException ex) {
        return buildErrorResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    //Any database exceptions
    @ExceptionHandler(JdbcSQLDataException.class)
    protected ResponseEntity<Object> handleEntityNotFound(JdbcSQLDataException ex) {
        return buildErrorResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR); //TODO: rethink status code
    }

    //Input validation errors
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ConstraintViolationException ex) {
        return buildErrorResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        String message = "Validation errors found: " + errors;
        return buildErrorResponseEntity(ex, HttpStatus.BAD_REQUEST, message);
    }


    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> catchAll(Exception ex) {
        return buildErrorResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(Exception ex, HttpStatus status) {
        return buildErrorResponseEntity(ex, status, ex.getMessage());
    }

    private ResponseEntity<Object> buildErrorResponseEntity(Exception ex, HttpStatus status, String message) {
        ApiError error = ApiError.builder()
                .status(status.name())
                .statusCode(status.value())
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
        return ResponseEntity.status(status)
                .body(ApiErrorResponse.builder().error(error).build());
    }
}