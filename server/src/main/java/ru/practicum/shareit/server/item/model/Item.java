package ru.practicum.shareit.server.item.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortComparator;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.utils.BookingMapper;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.model.Response;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {

    @Column(name = "item_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private Request request;

    @OneToMany(mappedBy = "bookedItem", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @SortComparator(BookingMapper.BookingsByStartComparator.class)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Comment> comments;

    @OneToOne(mappedBy = "item", fetch = FetchType.EAGER)
    private Response response;
}
