package ru.practicum.ewm.exception;

public class WrongStateForUpdateEvent extends Exception {
    public WrongStateForUpdateEvent() {
        super("Изменить можно только отмененные события или события в состоянии ожидания модерации)");
    }
}