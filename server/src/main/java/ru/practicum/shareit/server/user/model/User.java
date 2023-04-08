package ru.practicum.shareit.server.user.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.model.Response;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity(name = "users")
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @OneToMany(mappedBy = "booker")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    @OneToMany(mappedBy = "requestor")
    private List<Request> requests;

    @OneToMany(mappedBy = "owner")
    private List<Response> responses;
}
