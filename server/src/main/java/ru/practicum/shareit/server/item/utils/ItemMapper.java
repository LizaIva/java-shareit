package ru.practicum.shareit.server.item.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.utils.BookingMapper;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    private final CommentMapper commentMapper;
    private final UserStorage userStorage;
    private final RequestAndResponseStorage requestAndResponseStorage;
    private final BookingMapper bookingMapper;

    public ItemMapper(CommentMapper commentMapper,
                      UserStorage userStorage,
                      RequestAndResponseStorage requestAndResponseStorage,
                      @Autowired @Lazy BookingMapper bookingMapper) {

        this.commentMapper = commentMapper;
        this.userStorage = userStorage;
        this.requestAndResponseStorage = requestAndResponseStorage;
        this.bookingMapper = bookingMapper;
    }

    public ItemDto mapToItemDto(Item item, Integer userId) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(commentMapper.mapToCommentDto(item.getComments()))
                .build();

        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getRequestId());
        }

        if (item.getOwner().getId().equals(userId)) {
            List<Booking> bookings = item.getBookings();

            if (!bookings.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                Booking last = null;
                Booking next = null;

                for (Booking booking : bookings) {
                    if (Status.REJECTED.equals(booking.getStatus())) {
                        continue;
                    }

                    if (booking.getStart().isBefore(now)) {
                        if (last == null || last.getStart().isBefore(booking.getStart())) {
                            last = booking;
                        }
                    }

                    if (booking.getStart().isAfter(now)) {
                        if (next == null || next.getStart().isAfter(booking.getStart())) {
                            next = booking;
                        }
                    }
                }

                if (last != null) {
                    itemDto.setLastBooking(bookingMapper.mapToBookingDto(last));
                }
                if (next != null) {
                    itemDto.setNextBooking(bookingMapper.mapToBookingDto(next));
                }
            }
        }

        return itemDto;
    }

    public Item mapToItem(Integer ownerId, ItemDto itemDto) {
        Request request = requestAndResponseStorage.getRequestById(itemDto.getRequestId());
        User owner = userStorage.getUserById(ownerId);

        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(owner)
                .available(itemDto.getAvailable())
                .request(request)
                .build();
    }

    public List<ItemDto> mapToItemsDto(List<Item> items, Integer userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(mapToItemDto(item, userId));
        }
        return itemsDto;
    }
}
