package ru.practicum.ewm.server.event.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    @NotNull(message = "не указана широта ")
    private Double lat;
    @NotNull(message = "не указана долгота ")
    private Double lon;
}
