package ru.practicum.shareit.server.booking.dto;

import ru.practicum.shareit.server.exception.CheckStateException;

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
