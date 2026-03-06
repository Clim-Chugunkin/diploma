package ru.practicum.ewm.server.comment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "comment")
    private String comment;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;
}
