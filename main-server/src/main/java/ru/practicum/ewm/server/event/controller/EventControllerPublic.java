package ru.practicum.ewm.server.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.event.DTO.EventFullDto;
import ru.practicum.ewm.server.event.DTO.EventShortDto;
import ru.practicum.ewm.server.event.DTO.FilterDto;
import ru.practicum.ewm.server.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findWithFilters(@ModelAttribute FilterDto filter,
                                               HttpServletRequest request) {
        return eventService.findEventsWithFilters(filter, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable Long id,
                                 HttpServletRequest request) {
        return eventService.getEventById(id, request.getRemoteAddr(), request.getRequestURI());
    }
}
