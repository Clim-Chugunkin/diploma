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

    @Mapping(target = "location.lat", source = "event.lat")
    @Mapping(target = "location.lon", source = "event.lon")
    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    @Named("catFormatter")
    default Category toCategory(Long id) {
        Category cat = new Category();
        cat.setId(id);
        return cat;
    }
}
