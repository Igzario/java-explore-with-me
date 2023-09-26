package ru.practicum.ewm.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryAlreadyExists extends Exception {
    private final String message = "A category with the same name already exists";
}