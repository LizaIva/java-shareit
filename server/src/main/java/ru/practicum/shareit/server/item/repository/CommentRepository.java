package ru.practicum.shareit.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.item.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
