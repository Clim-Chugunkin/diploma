package ru.practicum.ewm.server.event.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.server.category.DTO.CategoryDto;
import ru.practicum.ewm.server.event.model.Event;
import ru.practicum.ewm.server.user.DTO.UserShortDto;

import static ru.practicum.ewm.server.utils.DateTimeFormat.formatter;


@Data
@NoArgsConstructor
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
