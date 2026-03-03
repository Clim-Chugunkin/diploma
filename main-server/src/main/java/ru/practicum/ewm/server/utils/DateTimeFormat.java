package ru.practicum.ewm.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormat {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String asString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : null;
    }

    public LocalDateTime fromString(String date) {
        return date != null ? LocalDateTime.parse(date, formatter) : null;
    }
}
