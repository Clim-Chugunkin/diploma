package ru.practicum.ewm.server.event.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.ewm.server.event.model.Location;

@Data
public class NewEventDto {
    @NotNull(message = "не указана аннотация события")
    @NotBlank(message = "аннотация не должна быть пустой")
    private String annotation;
    @NotNull(message = "не указана аннотация события")
    @Positive(message = "индекс категории должна быть положительным числом")
    private Long category;
    @NotNull(message = "не указана поле description события")
    @NotBlank(message = "поле description не должна быть пустой")
    private String description;
    @NotNull(message = "не указана дата события")
    private String eventDate;
    @Valid
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull(message = "не указана поле title события")
    @NotBlank(message = "поле title не должна быть пустой")
    private String title;
}
