package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventDateException extends Exception {
    private final String message = "Время события раньше чем через 2 часа";
}