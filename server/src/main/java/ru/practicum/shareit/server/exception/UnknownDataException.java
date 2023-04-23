package ru.practicum.shareit.server.exception;

public class UnknownDataException extends RuntimeException {
    public UnknownDataException(String message) {
        super(message);
    }
}