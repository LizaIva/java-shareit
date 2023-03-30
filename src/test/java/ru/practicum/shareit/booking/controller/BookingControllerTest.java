package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @SneakyThrows
    @Test
    void create() {
        int bookerId = 2;
        int itemId = 1;

        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
                .id(1)
                .itemId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();


        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(createBookingRequestDto.getStart())
                .end(createBookingRequestDto.getEnd())
                .status(Status.WAITING)
                .item(null)
                .booker(null)
                .build();

        when(bookingService.put(createBookingRequestDto, bookerId)).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);

        createBookingRequestDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setStart(createBookingRequestDto.getStart());

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isBadRequest());

        createBookingRequestDto.setStart(null);
        bookingDto.setStart(createBookingRequestDto.getStart());

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isBadRequest());

        createBookingRequestDto.setStart(LocalDateTime.now().plusDays(10));
        bookingDto.setStart(createBookingRequestDto.getStart());

        createBookingRequestDto.setEnd(null);
        bookingDto.setStart(createBookingRequestDto.getEnd());
        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isBadRequest());

        createBookingRequestDto.setEnd(LocalDateTime.now().minusDays(10));
        bookingDto.setStart(createBookingRequestDto.getEnd());
        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void updateStatus() {
        int ownerId = 1;
        int bookerId = 2;
        int itemId = 1;

        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
                .id(1)
                .itemId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();


        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(createBookingRequestDto.getStart())
                .end(createBookingRequestDto.getEnd())
                .status(Status.WAITING)
                .item(null)
                .booker(null)
                .build();

        when(bookingService.updateStatus(bookingDto.getId(), true, ownerId))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .param("approved", "true")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        int ownerId = 1;
        int bookerId = 2;
        int itemId = 1;

        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
                .id(1)
                .itemId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();


        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(createBookingRequestDto.getStart())
                .end(createBookingRequestDto.getEnd())
                .status(Status.WAITING)
                .item(null)
                .booker(null)
                .build();

        when(bookingService.getBookingById(bookingDto.getId(), ownerId)).thenReturn(bookingDto);
        when(bookingService.getBookingById(bookingDto.getId(), bookerId)).thenReturn(bookingDto);

        String result1 = mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result1);

        String result2 = mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result2);
    }

    @SneakyThrows
    @Test
    void getOwnersAllBookingsViaStatus() {
        int ownerId = 1;
        int bookerId = 2;
        int itemId = 1;

        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .status(Status.WAITING)
                .item(null)
                .booker(null)
                .build();

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(7))
                .end(LocalDateTime.now().plusDays(9))
                .status(Status.CANCELED)
                .item(null)
                .booker(null)
                .build();

        BookingDto bookingDto3 = BookingDto.builder()
                .id(3)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(13))
                .status(Status.REJECTED)
                .item(null)
                .booker(null)
                .build();

        List<BookingDto> bookingsDto = List.of(bookingDto1, bookingDto2, bookingDto3);

        when(bookingService.getOwnersAllBookingsViaStatus(ownerId, State.ALL, null, null)).thenReturn(bookingsDto);

        String result1 = mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingsDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingsDto), result1);
    }

    @SneakyThrows
    @Test
    void getBookersAllBooking() {
        int bookerId = 2;
        int itemId = 1;

        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .status(Status.WAITING)
                .item(null)
                .booker(null)
                .build();

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(7))
                .end(LocalDateTime.now().plusDays(9))
                .status(Status.CANCELED)
                .item(null)
                .booker(null)
                .build();

        BookingDto bookingDto3 = BookingDto.builder()
                .id(3)
                .bookerId(bookerId)
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(13))
                .status(Status.REJECTED)
                .item(null)
                .booker(null)
                .build();

        List<BookingDto> bookingsDto = List.of(bookingDto1, bookingDto2, bookingDto3);

        when(bookingService.getBookersAllBooking(bookerId, State.ALL, null, null)).thenReturn(bookingsDto);

        String result1 = mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingsDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingsDto), result1);

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "LALALA")
                )
                .andExpect(status().isBadRequest());
    }
}