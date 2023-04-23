package ru.practicum.shareit.server.exception;

public class CheckOwnerException extends RuntimeException {
    private final String message;

    public CheckOwnerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
