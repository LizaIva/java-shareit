package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void create() {
//        int ownerId = 1;
//        int bookerId = 2;
//        int itemId = 1;
//
//        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
//                .id(1)
//                .itemId(1)
//                .status(Status.WAITING)
//                .start(LocalDateTime.now().plusDays())
//                .build();
    }

    @Test
    void updateStatus() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getOwnersAllBookingsViaStatus() {
    }

    @Test
    void getBookersAllBooking() {
    }

    @Test
    void getItemsAllBooking() {
    }
}