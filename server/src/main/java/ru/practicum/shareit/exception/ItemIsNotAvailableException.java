package ru.practicum.shareit.exception;

public class ItemIsNotAvailableException extends RuntimeException {
    public ItemIsNotAvailableException(String message) {
        super(message);
    }
}
