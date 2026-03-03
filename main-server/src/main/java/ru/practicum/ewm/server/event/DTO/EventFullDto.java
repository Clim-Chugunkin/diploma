package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.user.DTO.UserShortDto;

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
}
