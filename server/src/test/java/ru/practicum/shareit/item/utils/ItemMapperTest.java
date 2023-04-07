package ru.practicum.shareit.item.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.utils.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private RequestAndResponseStorage requestAndResponseStorage;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    public void getItemWithLastAndNextBookingTest() {

        LocalDateTime now = LocalDateTime.now();

        Booking booking1 = Booking.builder()
                .id(1)
                .status(Status.REJECTED)
                .build();
        Booking booking2 = Booking.builder()
                .id(2)
                .status(Status.WAITING)
                .start(now.minusDays(1))
                .build();
        Booking booking3 = Booking.builder()
                .id(3)
                .status(Status.APPROVED)
                .start(now.plusDays(2))
                .build();
        Booking booking4 = Booking.builder()
                .id(4)
                .status(Status.APPROVED)
                .start(now.plusDays(1))
                .build();

        Item item = Item.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .request(Request.builder()
                        .requestId(1)
                        .build()
                )
                .owner(User.builder()
                        .id(1234)
                        .build())
                .bookings(List.of(booking1, booking2, booking3, booking4))
                .build();

        Mockito.when(bookingMapper.mapToBookingDto(booking2))
                .thenReturn(BookingDto.builder()
                        .id(booking2.getId())
                        .build()
                );

        Mockito.when(bookingMapper.mapToBookingDto(booking4))
                .thenReturn(BookingDto.builder()
                        .id(booking4.getId())
                        .build()
                );

        ItemDto itemDto = itemMapper.mapToItemDto(item, 1234);

        assertEquals(booking2.getId(), itemDto.getLastBooking().getId());
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        return user;
    }
}