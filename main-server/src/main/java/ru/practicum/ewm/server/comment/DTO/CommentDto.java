package ru.practicum.ewm.server.comment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String eventDescription;
    private String comment;
    private String created;
    private String author;
}
