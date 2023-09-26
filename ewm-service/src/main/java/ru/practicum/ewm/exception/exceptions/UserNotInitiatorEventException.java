package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotInitiatorEventException extends Exception {
    private final String message;

    public UserNotInitiatorEventException(Long eventId, Long userId) {
        this.message = "User with ID " + userId + ", is not the creator of the event with ID " + eventId;
    }
}