package ru.practicum.ewm.server.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.ewm.server.category.model.Category;
import ru.practicum.ewm.server.event.DTO.EventFullDto;
import ru.practicum.ewm.server.event.DTO.EventShortDto;
import ru.practicum.ewm.server.event.DTO.NewEventDto;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.utils.DateTimeFormat;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateTimeFormat.class)
public interface EventMapper {

    @Mapping(source = "category", target = "category", qualifiedByName = "catFormatter")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "eventDto.location.lat", target = "lat")
    @Mapping(source = "eventDto.location.lon", target = "lon")
    @Mapping(target = "state", expression = "java(ru.practicum.ewm.server.event.model.State.PENDING)")
    Event toEvent(NewEventDto eventDto);

    //    public static EventFullDto fromEventToFullEvent(Event event) {
//        EventFullDto eventFull = new EventFullDto();
//        eventFull.setId(event.getId());
//        eventFull.setAnnotation(event.getAnnotation());
//        //eventFull.setCategory(CategoryMapper.fromCategoryToCategoryDto(event.getCategory()));
//        eventFull.setCreatedOn(event.getCreatedOn().format(formatter));
//        eventFull.setDescription(event.getDescription());
//        eventFull.setEventDate(event.getEventDate().format(formatter));
//        //eventFull.setInitiator(UserMapper.fromUserToUserShortDto(event.getInitiator()));
//        eventFull.setLocation(new Location(event.getLat(), event.getLon()));
//        eventFull.setPaid(event.getPaid());
//        eventFull.setParticipantLimit(event.getParticipantLimit());
//        if (event.getPublishedOn() != null) {
//            eventFull.setPublishedOn(event.getPublishedOn().format(formatter));
//        }
//        eventFull.setRequestModeration(event.getRequestModeration());
//        eventFull.setTitle(event.getTitle());
//        eventFull.setState(event.getState());
//        return eventFull;
//    }
    @Mapping(target = "location.lat", source = "event.lat")
    @Mapping(target = "location.lon", source = "event.lon")
    EventFullDto toEventFullDto(Event event);

    //    public static EventShortDto fromEventToShortEvent(Event event) {
//        EventShortDto eventShort = new EventShortDto();
//        eventShort.setId(event.getId());
//        eventShort.setAnnotation(event.getAnnotation());
//        //eventShort.setCategory(CategoryMapper.fromCategoryToCategoryDto(event.getCategory()));
//        eventShort.setEventDate(event.getEventDate().format(formatter));
//        //eventShort.setInitiator(UserMapper.fromUserToUserShortDto(event.getInitiator()));
//        eventShort.setPaid(event.getPaid());
//        eventShort.setTitle(event.getTitle());
//        return eventShort;
//    }
    EventShortDto toEventShortDto(Event event);


    //    public static Event joinEventWithUpdateEvent(Event event, UpdateEventUserRequest request) {
//        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
//        if (request.getCategory() != null) {
//            Category cat = new Category();
//            cat.setId(request.getCategory());
//            event.setCategory(cat);
//        }
//        if (request.getDescription() != null) event.setDescription(request.getDescription());
//        if (request.getEventDate() != null) {
//            event.setEventDate(LocalDateTime.parse(request.getEventDate(), formatter));
//        }
//        if (request.getLocation() != null) {
//            event.setLat(request.getLocation().getLat());
//            event.setLon(request.getLocation().getLon());
//        }
//        if (request.getPaid() != null) event.setPaid(request.getPaid());
//        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
//        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
//        if (request.getTitle() != null) event.setTitle(request.getTitle());
//        return event;
//    }
    @Named("catFormatter")
    default Category toCategory(Long id) {
        Category cat = new Category();
        cat.setId(id);
        return cat;
    }
}
