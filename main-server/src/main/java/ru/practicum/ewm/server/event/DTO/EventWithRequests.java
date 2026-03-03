package ru.practicum.ewm.server.event.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.server.event.model.Event;

@AllArgsConstructor
@Data
public class EventWithRequests {
    private Event event;
    private Long confirmedRequests;
}
