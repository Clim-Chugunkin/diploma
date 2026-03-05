package ru.practicum.ewm.server.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class ErrorMessage {
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
