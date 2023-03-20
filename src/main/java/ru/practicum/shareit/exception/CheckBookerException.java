package ru.practicum.shareit.exception;

public class CheckBookerException extends RuntimeException {
    private final String message;

    public CheckBookerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
