package ru.practicum.ewm.server.event.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class FilterDto {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String text;
    private Integer[] users;
    private String[] states;
    private Integer[] categories;
    private Boolean paid;
    private String sort;
    private Boolean onlyAvailable = false;
    private String rangeStart;
    private String rangeEnd;
    private Integer from = 0;
    private Integer size = 10;

    public LocalDateTime getStart() {
        if (rangeStart == null) return null;
        return LocalDateTime.parse(rangeStart, formatter);
    }

    public LocalDateTime getEnd() {
        if (rangeEnd == null) return null;
        return LocalDateTime.parse(rangeEnd, formatter);
    }

}
