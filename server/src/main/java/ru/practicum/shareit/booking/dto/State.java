package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.exception.CheckStateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State fromValue(String value) {
        for (State state : State.values()) {
            if (state.name().equals(value)) {
                return state;
            }
        }
        throw new CheckStateException(String.format("Unknown state: %s", value));
    }
}
