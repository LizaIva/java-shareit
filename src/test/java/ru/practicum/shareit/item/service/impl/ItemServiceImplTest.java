package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.utils.ItemMapper;
import ru.practicum.shareit.request.model.Response;
import ru.practicum.shareit.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.utils.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemStorage itemStorage;
    @Mock
    private RequestAndResponseStorage requestAndResponseStorage;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserStorage userStorage;
    @Mock
    private UserServiceImpl userService;
    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void put() {
        User user = createUser();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .requestId(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        Item item = new Item();
        item.setId(1);
        item.setName("Молоток");
        item.setDescription("Для гвоздей");
        item.setOwner(user);
        item.setAvailable(true);

        Mockito.when(itemMapper.mapToItem(user.getId(), itemDto)).thenReturn(item);

        itemService.put(user.getId(), itemDto);

        Mockito.verify(userStorage, Mockito.times(1)).checkUser(user.getId());
        Mockito.verify(itemStorage, Mockito.times(1)).put(user.getId(), item);
        Mockito.verify(requestAndResponseStorage, Mockito.times(1)).put(Mockito.any(Response.class));
    }

    @Test
    void getOwnersItems() {
    }

    @Test
    void foundAvailableItemWithNameOrDescription() {
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        return user;
    }
}