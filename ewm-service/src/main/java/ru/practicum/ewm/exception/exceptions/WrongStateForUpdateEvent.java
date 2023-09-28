package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WrongStateForUpdateEvent extends Exception {
    private final String message =
            "Only canceled events or events pending moderation can be changed";
}