package ru.practicum.ewm.exception;

public class NameAlreadyExists extends Exception {
    public NameAlreadyExists(Class entity) {
        super(entity.getSimpleName() + " c таким именем уже существует");
    }
}