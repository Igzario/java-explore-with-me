package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventNotPublishedException extends Exception {
    private final String message = "The event is not published and cannot be output from the public endpoint";
}