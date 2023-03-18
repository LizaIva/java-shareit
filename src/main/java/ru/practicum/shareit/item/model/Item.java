package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.annotations.SortComparator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.utils.BookingMapper;
import ru.practicum.shareit.user.model.User;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "bookedItem", fetch = FetchType.LAZY)
    @SortComparator(BookingMapper.BookingsByStartComparator.class)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Comment> comments;
}
