package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.category.mapper.CategoryMapper;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.user.DTO.UserShortDto;
import ru.practicum.ewm.server.user.mapper.UserMapper;

import static ru.practicum.ewm.server.utils.DateTimeFormat.formatter;


@Data
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequest;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;

    public EventShortDto(Event event, Long confirmedRequest) {
        this.id = event.getId();
        this.annotation = event.getAnnotation();
        this.confirmedRequest = confirmedRequest;
        this.category = CategoryMapper.fromCategoryToCategoryDto(event.getCategory());
        this.eventDate = event.getEventDate().format(formatter);
        this.initiator = UserMapper.fromUserToUserShortDto(event.getInitiator());
        this.paid = event.getPaid();
        this.title = event.getTitle();
    }
}
