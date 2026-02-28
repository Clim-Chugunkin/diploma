package ru.practicum.ewm.server.request.mapper;

import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;
import ru.practicum.ewm.server.request.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto fromRequestToRequestDto(Request request) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEvent());
        requestDto.setCreated(request.getCreated().format(formatter));
        requestDto.setRequester(request.getRequestor());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

}
