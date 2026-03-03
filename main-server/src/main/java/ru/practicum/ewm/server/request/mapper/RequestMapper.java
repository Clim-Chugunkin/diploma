package ru.practicum.ewm.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;
import ru.practicum.ewm.server.request.model.Request;
import ru.practicum.ewm.server.utils.DateTimeFormat;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateTimeFormat.class)
public interface RequestMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    public static ParticipationRequestDto fromRequestToRequestDto(Request request) {
//        ParticipationRequestDto requestDto = new ParticipationRequestDto();
//        requestDto.setId(request.getId());
//        requestDto.setEvent(request.getEvent());
//        requestDto.setCreated(request.getCreated().format(formatter));
//        requestDto.setRequester(request.getRequestor());
//        requestDto.setStatus(request.getStatus());
//        return requestDto;
//    }

    ParticipationRequestDto toParticipationRequestDto(Request request);
}
