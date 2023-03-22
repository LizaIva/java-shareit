package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.Response;

import java.util.List;

public interface RequestAndResponseStorage {

    String REQUEST_NOT_FOUND = "Request с id = %s не найден.";
    String NOT_EXIST = "Нельзя сохранить запрос без данных";
    Request put(Request request);

    Response put(Response response);

    Request getRequestById(Integer id);

    List<Request> getAll();

    List<Request> getAllWithOffset(Integer size, Integer from);

    List<Request> getAllByUserIdWithOffset(Integer size, Integer from, Integer userId);
}
