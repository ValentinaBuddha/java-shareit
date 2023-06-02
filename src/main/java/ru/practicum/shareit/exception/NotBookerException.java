package ru.practicum.shareit.exception;

public class NotBookerException extends RuntimeException {
    public NotBookerException(String message) {
        super(message);
    }
}