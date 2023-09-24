package ru.practicum.ewm.exception.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends Exception {
    private final String message;

    public ValidationException(String message) {
        this.message = message;
    }
}