package ru.practicum.ewm.server.comment.service;

import ru.practicum.ewm.server.comment.DTO.CommentDto;
import ru.practicum.ewm.server.comment.DTO.NewCommentRequest;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllByEventId(Long eventId);

    List<CommentDto> getAllByUserId(Long userId);

    CommentDto addComment(Long userId, Long eventId, NewCommentRequest request);

    CommentDto updateComment(Long userId, Long commentId, NewCommentRequest request);

    void deleteComment(Long userId, Long commentId);

    CommentDto getById(Long commentId);
}
