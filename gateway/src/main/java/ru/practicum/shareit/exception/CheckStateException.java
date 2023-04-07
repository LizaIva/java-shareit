package ru.practicum.shareit.exception;

public class CheckStateException extends RuntimeException {
    private final String message;

    public CheckStateException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}