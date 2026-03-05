package ru.practicum.ewm.server.event.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.user.DTO.UserShortDto;


@Getter
@Setter
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
