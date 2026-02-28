package ru.practicum.ewm.server.event.mapper;

import ru.practicum.ewm.server.category.mapper.CategoryMapper;
import ru.practicum.ewm.server.category.model.Category;
import ru.practicum.ewm.server.event.DTO.EventFullDto;
import ru.practicum.ewm.server.event.DTO.EventShortDto;
import ru.practicum.ewm.server.event.DTO.NewEventDto;
import ru.practicum.ewm.server.event.DTO.UpdateEventUserRequest;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.State;
import ru.practicum.ewm.server.user.mapper.UserMapper;
import ru.practicum.ewm.server.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.ewm.server.utils.DateTimeFormat.formatter;

public class EventMapper {


    public static Event fromNewEventToEvent(NewEventDto eventDto, User user, Category cat) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(cat);
        event.setInitiator(user);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), formatter));
        event.setCreatedOn(LocalDateTime.now());
        event.setLat(eventDto.getLocation().getLat());
        event.setLon(eventDto.getLocation().getLon());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setState(State.PENDING);
        return event;
    }

    public static EventFullDto fromEventToFullEvent(Event event) {
        EventFullDto eventFull = new EventFullDto();
        eventFull.setId(event.getId());
        eventFull.setAnnotation(event.getAnnotation());
        eventFull.setCategory(CategoryMapper.fromCategoryToCategoryDto(event.getCategory()));
        eventFull.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFull.setDescription(event.getDescription());
        eventFull.setEventDate(event.getEventDate().format(formatter));
        eventFull.setInitiator(UserMapper.fromUserToUserShortDto(event.getInitiator()));
        eventFull.setLocation(new Location(event.getLat(), event.getLon()));
        eventFull.setPaid(event.getPaid());
        eventFull.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventFull.setPublishedOn(event.getPublishedOn().format(formatter));
        }
        eventFull.setRequestModeration(event.getRequestModeration());
        eventFull.setTitle(event.getTitle());
        eventFull.setState(event.getState());
        return eventFull;
    }

    public static EventShortDto fromEventToShortEvent(Event event) {
        EventShortDto eventShort = new EventShortDto();
        eventShort.setId(event.getId());
        eventShort.setAnnotation(event.getAnnotation());
        eventShort.setCategory(CategoryMapper.fromCategoryToCategoryDto(event.getCategory()));
        eventShort.setEventDate(event.getEventDate().format(formatter));
        eventShort.setInitiator(UserMapper.fromUserToUserShortDto(event.getInitiator()));
        eventShort.setPaid(event.getPaid());
        eventShort.setTitle(event.getTitle());
        return eventShort;
    }


    public static Event joinEventWithUpdateEvent(Event event, UpdateEventUserRequest request) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (request.getCategory() != null) {
            Category cat = new Category();
            cat.setId(request.getCategory());
            event.setCategory(cat);
        }
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), formatter));
        }
        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        return event;
    }
}
