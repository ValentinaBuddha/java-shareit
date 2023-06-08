package ru.practicum.shareit.exception;

public class WrongNumbersForPagingException extends RuntimeException {
    public WrongNumbersForPagingException(String message) {
        super(message);
    }
}
