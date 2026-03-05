package ru.practicum.ewm.server.event.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.event.model.Location;

@Getter
@Setter
public class NewEventDto {
    @NotNull(message = "не указана аннотация события")
    @NotBlank(message = "аннотация не должна быть пустой")
    @Size(min = 20, max = 2000, message = "annotation должна быть от 20 до 2000 символов")
    private String annotation;

    @NotNull(message = "не указана аннотация события")
    @Positive(message = "индекс категории должна быть положительным числом")
    private Long category;

    @NotNull(message = "не указана поле description события")
    @NotBlank(message = "поле description не должна быть пустой")
    @Size(min = 20, max = 7000, message = "description должна быть от 20 до 7000 символов")
    private String description;

    @NotNull(message = "не указана дата события")
    private String eventDate;

    @Valid
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero(message = "лимит участников должен быть положительным")
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotNull(message = "не указана поле title события")
    @NotBlank(message = "поле title не должна быть пустой")
    @Size(min = 3, max = 120, message = "title должна быть от 3 до 120 символов")
    private String title;
}
