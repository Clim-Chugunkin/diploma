package ru.practicum.ewm.server.event.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.event.model.Event;

@AllArgsConstructor
@Getter
@Setter
public class EventWithRequests {
    private Event event;
    private Long confirmedRequests;
}
