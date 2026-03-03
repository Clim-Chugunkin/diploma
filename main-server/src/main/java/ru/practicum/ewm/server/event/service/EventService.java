package ru.practicum.ewm.server.event.service;

import ru.practicum.ewm.server.event.DTO.*;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(NewEventDto newEvent, Long userId);

    EventFullDto getById(Long userId, Long eventId);

    List<EventShortDto> getAllUserEvents(Long userId, int from, int size);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updatedEvent);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventUserRequest updatedEvent);

    List<EventFullDto> findEvents(FilterDto filter);

    List<EventShortDto> findEventsWithFilters(FilterDto filterDto, String ip, String uri);

    EventFullDto getEventById(Long id, String ip, String uri);
}
