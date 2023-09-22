package com.wex.purchasetransaction.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    ApiError error;
}
