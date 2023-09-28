package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAlreadyExists extends Exception {
    private final String message = "A user with this email already exists";
}