package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.StateAction;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
