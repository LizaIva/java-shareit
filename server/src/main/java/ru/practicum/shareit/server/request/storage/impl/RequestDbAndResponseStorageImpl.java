package ru.practicum.shareit.server.request.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.exception.UnknownDataException;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.model.Response;
import ru.practicum.shareit.server.request.repository.RequestRepository;
import ru.practicum.shareit.server.request.repository.ResponseRepository;
import ru.practicum.shareit.server.request.storage.RequestAndResponseStorage;

import java.util.List;

@RequiredArgsConstructor
@Component("requestDbStorageImpl")
public class RequestDbAndResponseStorageImpl implements RequestAndResponseStorage {

    private static final Sort PAGIN_SORT = Sort.by(List.of(Sort.Order.asc("created")));

    private final RequestRepository requestRepository;
    private final ResponseRepository responseRepository;

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
