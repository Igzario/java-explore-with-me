package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventDateException extends Exception {
    private final String message = "Event time is less than 2 hours later";
}