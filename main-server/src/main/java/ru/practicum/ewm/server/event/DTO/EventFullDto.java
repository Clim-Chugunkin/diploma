package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.mapper.CategoryMapper;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.user.DTO.UserShortDto;
import ru.practicum.ewm.server.user.mapper.UserMapper;

import static ru.practicum.ewm.server.utils.DateTimeFormat.formatter;

@Data
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String title;
    private State state;
    private Long views;

    public EventFullDto(Event event, Long confirmedRequest) {
        this.id = event.getId();
        this.annotation = event.getAnnotation();
        this.confirmedRequests = confirmedRequest;
        this.category = CategoryMapper.fromCategoryToCategoryDto(event.getCategory());
        this.createdOn = event.getCreatedOn().format(formatter);
        this.description = event.getDescription();
        this.eventDate = event.getEventDate().format(formatter);
        this.initiator = UserMapper.fromUserToUserShortDto(event.getInitiator());
        this.location = new Location(event.getLat(), event.getLon());
        this.paid = event.getPaid();
        this.participantLimit = event.getParticipantLimit();
        this.publishedOn = event.getPublishedOn().format(formatter);
        this.requestModeration = event.getRequestModeration();
        this.title = event.getTitle();
        this.state = event.getState();
    }

}
