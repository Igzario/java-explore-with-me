package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NameAlreadyExists extends Exception {
    private final String message;

    public NameAlreadyExists(Class entity) {
        this.message = entity.getSimpleName() +
                " c таким именем уже существует";
    }
}