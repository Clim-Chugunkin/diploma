package ru.practicum.ewm.server.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.ewm.server.comment.DTO.CommentDto;
import ru.practicum.ewm.server.comment.model.Comment;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.user.model.User;
import ru.practicum.ewm.server.utils.DateTimeFormat;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateTimeFormat.class)
public interface CommentMapper {
    @Mapping(source = "author", target = "author", qualifiedByName = "userFormatter")
    @Mapping(source = "event", target = "eventDescription", qualifiedByName = "eventFormatter")
    CommentDto toCommentDto(Comment comment);

    @Named("userFormatter")
    default String toAuthor(User user) {
        return user.getName();
    }

    @Named("eventFormatter")
    default String toEventDescription(Event event) {
        return event.getDescription();
    }
}
