package ru.practicum.ewm.server.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.event.DTO.EventFullDto;
import ru.practicum.ewm.server.event.DTO.FilterDto;
import ru.practicum.ewm.server.event.DTO.UpdateEventUserRequest;
import ru.practicum.ewm.server.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEvents(@ModelAttribute FilterDto filter) {
        return eventService.findEvents(filter);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updatedEvent) {
        return eventService.updateEventByAdmin(eventId, updatedEvent);
    }
}
