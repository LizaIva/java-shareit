package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "requests")
public class Request {
    @Column(name = "request_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(mappedBy = "request")
    private List<Response> responses;
}
