package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WrongStateForUpdateEvent extends Exception {
    private final String message =
            "Изменить можно только отмененные события или события в состоянии ожидания модерации";
}