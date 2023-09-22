package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CategoryAlreadyExists extends Exception {
    public CategoryAlreadyExists() {
        super("Категория с таким названием уже существует");
    }
}