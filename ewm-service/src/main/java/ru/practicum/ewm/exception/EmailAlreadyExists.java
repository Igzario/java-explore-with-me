package ru.practicum.ewm.exception;


public class EmailAlreadyExists extends Exception {
    public EmailAlreadyExists() {
        super("Пользователь с таким email уже существует");
    }
}