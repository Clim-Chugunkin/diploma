package ru.practicum.ewm.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.server.request.DTO.ParticipationRequestDto;
import ru.practicum.ewm.server.request.model.Request;
import ru.practicum.ewm.server.utils.DateTimeFormat;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateTimeFormat.class)
public interface RequestMapper {
    ParticipationRequestDto toParticipationRequestDto(Request request);
}
