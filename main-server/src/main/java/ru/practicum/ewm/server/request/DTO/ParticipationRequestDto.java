package ru.practicum.ewm.server.request.DTO;

import lombok.Data;
import ru.practicum.ewm.server.request.model.Status;

@Data
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requestor;
    private Status status;
}
