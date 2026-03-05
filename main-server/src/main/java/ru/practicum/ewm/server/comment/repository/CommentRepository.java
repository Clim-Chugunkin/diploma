package ru.practicum.ewm.server.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.server.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEvent_id(Long eventId);

    List<Comment> findAllByAuthor_id(Long userId);
}
