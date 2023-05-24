package ru.practicum.shareit.exception;

public class WrongDatesException extends RuntimeException {
    public WrongDatesException(String message) {
        super(message);
    }
}
