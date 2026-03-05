package ru.practicum.ewm.server.event.DTO;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.event.model.Location;
import ru.practicum.ewm.server.event.model.StateAction;

@Getter
@Setter
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "annotation должна быть от 20 до 2000 символов")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "description должна быть от 20 до 7000 символов")
    private String description;

    private String eventDate;
    private Location location;
    private Boolean paid;

    @Positive(message = "лимит участников должен быть положительным")
    private Integer participantLimit;

    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(min = 3, max = 120, message = "title должна быть от 3 до 120 символов")
    private String title;
}
