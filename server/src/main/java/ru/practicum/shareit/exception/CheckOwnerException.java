package ru.practicum.shareit.exception;

public class CheckOwnerException extends RuntimeException {
    private final String message;

    public CheckOwnerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
