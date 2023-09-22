package com.wex.purchasetransaction.exception;

import org.h2.jdbc.JdbcSQLDataException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return buildErrorResponseEntity(ex, HttpStatus.BAD_REQUEST, "Invalid input format. Please check if the date and number formats are correct.");
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ExchangeRateNotFoundException ex) {
        return buildErrorResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    //Any database exceptions
    @ExceptionHandler(JdbcSQLDataException.class)
    protected ResponseEntity<Object> handleEntityNotFound(JdbcSQLDataException ex) {
        return buildErrorResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR); //TODO: rethink status code
    }

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