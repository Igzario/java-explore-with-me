package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventNotPublishedException extends Exception {
    private final String message = "Событие не опубликовано и не может быть выведено по публичному эндпоинту";
}