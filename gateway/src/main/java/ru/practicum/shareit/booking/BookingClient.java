package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;


@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();

        String url = "";

        return createUrl(userId, state, from, size, parameters, url);
    }

    public ResponseEntity<Object> getOwnerBookings(Integer userId, State state, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();

        String url = "/owner";

        return createUrl(userId, state, from, size, parameters, url);
    }

    private ResponseEntity<Object> createUrl(Integer userId, State state, Integer from, Integer size, Map<String, Object> parameters, String url) {
        if (state != null) {
            parameters.put("state", state.name());
            url = url + "?state={state}";
        }
        if (from != null || size != null) {
            parameters.put("from", from);
            parameters.put("size", size);
            if (url.equals("")) {
                url = url + "?";
            } else {
                url = url + "&";
            }
            url = url + "from={from}&size={size}";
        }
        return get(url, userId, parameters);
    }


    public ResponseEntity<Object> bookItem(Integer userId, CreateBookingRequestDto createBookingRequestDto) {
        return post("", userId, createBookingRequestDto);
    }

    public ResponseEntity<Object> updateStatus(int bookingId, boolean approved, int ownerId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(Integer userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }
}