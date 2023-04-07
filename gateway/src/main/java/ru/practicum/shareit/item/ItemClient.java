package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Integer ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> createComment(Integer itemId, Integer userId, CreateCommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> update(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto) {
        return patch("/" + itemId, ownerId, updatedItemDto);
    }

    public ResponseEntity<Object> getItemById(Integer userId, Integer itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getOwnersItems(Integer ownerId, Integer from, Integer size) {
        if (from == null || size == null) {
            return get("", ownerId);
        } else {

            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("/?from={from}&size={size}", ownerId, parameters);
        }
    }

    public ResponseEntity<Object> foundItemWithText(String text, Integer from, Integer size) {
        if (from == null || size == null) {
            Map<String, Object> parameters = Map.of(
                    "text", text
            );
            return get("/search?text={text}", null, parameters);
        } else {
            Map<String, Object> parameters = Map.of(
                    "text", text,
                    "from", from,
                    "size", size
            );
            return get("/search?from={from}&size={size}", null, parameters);
        }
    }
}
