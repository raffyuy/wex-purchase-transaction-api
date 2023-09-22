package com.wex.purchasetransaction.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    String status;
    Integer statusCode;
    LocalDateTime timestamp;
    String message;
}
