package ru.practicum.ewm.exception;

import lombok.*;

@Data
@AllArgsConstructor
public class ApiError {
    private StackTraceElement[] errors;
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
