package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityNotFoundException extends Exception {
    private Class objClass;
    private long id;
}