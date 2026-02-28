package ru.practicum.ewm.server.event.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    @NotNull(message = "не указана широта ")
    @Positive(message = "широта должна быть положительной")
    private Double lat;
    @NotNull(message = "не указана долгота ")
    @Positive(message = "долгота должна быть положительной")
    private Double lon;
}
