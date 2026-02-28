package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.user.DTO.UserShortDto;

@Data
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequest;
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
}
