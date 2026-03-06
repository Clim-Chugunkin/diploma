package ru.practicum.ewm.server.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.server.comment.DTO.CommentDto;
import ru.practicum.ewm.server.comment.DTO.NewCommentRequest;
import ru.practicum.ewm.server.comment.mapper.CommentMapper;
import ru.practicum.ewm.server.comment.model.Comment;
import ru.practicum.ewm.server.comment.repository.CommentRepository;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.event.repository.EventRepository;
import ru.practicum.ewm.server.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.server.exceptions.ConflictException;
import ru.practicum.ewm.server.user.model.User;
import ru.practicum.ewm.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getAllByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события нет"));
        return commentRepository.findAllByEvent_id(eventId).stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Override
    public List<CommentDto> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя нет"));
        return commentRepository.findAllByAuthor_id(userId).stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя нет"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ConditionsNotMetException("события нет"));
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Событие не опубликовано");
        }
        Comment newComment = new Comment();
        newComment.setEvent(event);
        newComment.setAuthor(user);
        newComment.setComment(request.getMessage());
        newComment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(newComment));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя нет"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ConditionsNotMetException("такого комментария нет"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Пользователь не является создателем комментария");
        }
        comment.setComment(request.getMessage());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ConditionsNotMetException("пользователя нет"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ConditionsNotMetException("такого комментария нет"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Пользователь не является создателем комментария");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto getById(Long commentId) {
        return commentMapper.toCommentDto(commentRepository.findById(commentId).orElseThrow(() -> new ConditionsNotMetException("такогог комента нет")));
    }
}
