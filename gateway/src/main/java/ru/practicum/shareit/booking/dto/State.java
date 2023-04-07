package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.practicum.shareit.exception.CheckStateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    @JsonCreator
    public static State fromValue(String value) {
        for (State state : State.values()) {
            if (state.name().equals(value)) {
                return state;
            }
        }
        throw new CheckStateException(String.format("Unknown state: %s", value));
    }
}
