package ru.practicum.ewm.exception;

public class UserNotInitiatorEventException extends Exception {
    public UserNotInitiatorEventException(Long eventId, Long userId) {
        super("User c ID " + userId + ", не является создателем события с ID " + eventId);
    }
}