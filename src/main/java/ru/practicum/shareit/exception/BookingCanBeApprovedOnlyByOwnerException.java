package ru.practicum.shareit.exception;

public class BookingCanBeApprovedOnlyByOwnerException extends RuntimeException {
    public BookingCanBeApprovedOnlyByOwnerException(String message) {
        super(message);
    }
}