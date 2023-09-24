package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncorrectRequestException extends Exception {
    private final String message = "Запрос составлен не корректно";
}