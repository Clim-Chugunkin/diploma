package ru.practicum.ewm.server.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.event.DTO.EventFullDto;
import ru.practicum.ewm.server.event.DTO.EventShortDto;
import ru.practicum.ewm.server.event.DTO.NewEventDto;
import ru.practicum.ewm.server.event.DTO.UpdateEventUserRequest;
import ru.practicum.ewm.server.event.service.EventService;
import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.server.request.DTO.EventRequestStatusUpdateResult;
import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;
import ru.practicum.ewm.server.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEvent) {
        return eventService.addEvent(newEvent, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return eventService.getById(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getAllUserEvents(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllUserEvents(userId, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @RequestBody UpdateEventUserRequest updatedEvent) {
        return eventService.updateEventByUser(userId, eventId, updatedEvent);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByEvent(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        return requestService.getAllRequestByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest request) {
        return requestService.changeStatus(userId, eventId, request);
    }

}
