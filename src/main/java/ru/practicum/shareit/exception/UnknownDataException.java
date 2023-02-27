package ru.practicum.shareit.exception;

public class UnknownDataException extends RuntimeException {
    public UnknownDataException(String message) {
        super(message);
    }
}