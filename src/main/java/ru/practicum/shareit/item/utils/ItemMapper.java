package ru.practicum.shareit.item.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.utils.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;
    private final UserStorage userStorage;
    private final RequestAndResponseStorage requestAndResponseStorage;
    private final ItemStorage itemStorage;

    @Autowired
    @Lazy
    private BookingMapper bookingMapper;

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


    public User mapToUser(Integer id, UpdateUserDto dto) {
        return User.builder()
                .id(id)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public Item mapToItem(Integer ownerId, Integer itemId, UpdatedItemDto dto) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .owner(userStorage.getUserById(ownerId))
                .available(dto.getAvailable())
                .build();
    }
}
