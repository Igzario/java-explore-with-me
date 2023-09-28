package ru.practicum.ewm.exception.exceptions;

import lombok.Getter;

@Getter
public class CategoryDeleteException extends Exception {
    private final String message = "Category has associated events";
}
