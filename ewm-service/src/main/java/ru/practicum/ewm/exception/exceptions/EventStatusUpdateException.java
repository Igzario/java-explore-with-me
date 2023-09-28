package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.utility.State;

@Getter
@AllArgsConstructor
public class EventStatusUpdateException extends Exception {
    private final String message;

    public EventStatusUpdateException(State state) {
        this.message = "Unable to publish, event has status: " + state;
    }
}
