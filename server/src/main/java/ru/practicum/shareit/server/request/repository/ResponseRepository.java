package ru.practicum.shareit.server.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.request.model.Response;

public interface ResponseRepository extends JpaRepository<Response, Integer> {
}
