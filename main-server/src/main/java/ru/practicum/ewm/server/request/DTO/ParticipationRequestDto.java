package ru.practicum.ewm.server.request.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.request.model.Status;

@Getter
@Setter
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private Status status;
}
