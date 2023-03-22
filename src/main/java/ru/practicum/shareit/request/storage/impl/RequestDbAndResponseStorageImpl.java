package ru.practicum.shareit.request.storage.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.Response;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.repository.ResponseRepository;
import ru.practicum.shareit.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.awt.print.Pageable;
import java.util.List;

@RequiredArgsConstructor
@Component("requestDbStorageImpl")
public class RequestDbAndResponseStorageImpl implements RequestAndResponseStorage {

    private static final Sort PAGIN_SORT = Sort.by(List.of(Sort.Order.asc("created")));

    private final RequestRepository requestRepository;
    private final ResponseRepository responseRepository;
    private final UserStorage userStorage;

    @Override
    public Request put(Request request) {
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public Response put(Response response) {
        return responseRepository.saveAndFlush(response);
    }

    @Override
    public Request getRequestById(Integer id) {
        if (id == null) {
            return null;
        }

        return requestRepository.findById(id).orElseThrow(() -> new UnknownDataException(String.format(REQUEST_NOT_FOUND, id)));
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    @Override
    public List<Request> getAllWithOffset(Integer size, Integer from) {
        return requestRepository.findAll(PageRequest.of(from, size, PAGIN_SORT))
                .getContent();
    }

    @Override
    public List<Request> getAllByUserIdWithOffset(Integer size, Integer from, Integer userId) {
        return requestRepository.findAlByUserIdlWithOffset(userId, PageRequest.of(from, size, PAGIN_SORT))
                .getContent();
    }
}
