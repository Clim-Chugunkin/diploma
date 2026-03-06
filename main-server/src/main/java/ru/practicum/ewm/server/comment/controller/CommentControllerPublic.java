package ru.practicum.ewm.server.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.comment.DTO.CommentDto;
import ru.practicum.ewm.server.comment.DTO.NewCommentRequest;
import ru.practicum.ewm.server.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentControllerPublic {
    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable(name = "eventId") Long eventId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody NewCommentRequest request) {
        return commentService.addComment(userId, eventId, request);
    }

    //get all comments by eventId
    @GetMapping("/event/{eventId}")
    public List<CommentDto> getAllByEventId(@PathVariable(name = "eventId") Long eventId) {
        return commentService.getAllByEventId(eventId);
    }

    //get all comments by userId
    @GetMapping("/user/{userId}")
    public List<CommentDto> getAllByUserId(@PathVariable Long userId) {
        return commentService.getAllByUserId(userId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getById(@PathVariable(name = "commentId") Long commentId) {
        return commentService.getById(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable(name = "commentId") Long commentId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody NewCommentRequest request) {
        return commentService.updateComment(userId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(name = "commentId") Long commentId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        commentService.deleteComment(userId, commentId);
    }
}
