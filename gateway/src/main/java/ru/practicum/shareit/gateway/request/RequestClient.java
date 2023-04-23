package ru.practicum.shareit.gateway.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.request.dto.CreateRequestDto;
import ru.practicum.shareit.gateway.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestClient extends BaseClient {
    public RequestClient(RestTemplate rest) {
        super(rest);
    }

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(Integer requestorId, CreateRequestDto createRequestDto) {
        return post("", requestorId, createRequestDto);
    }

    public ResponseEntity<Object> getRequestById(Integer userId, Integer requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAll(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllWithOffset(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();

        String url = "/all";
        if (from != null || size != null) {
            parameters.put("from", from);
            parameters.put("size", size);

            url = url + "?from={from}&size={size}";
        }
        return get(url, userId, parameters);
    }
}
