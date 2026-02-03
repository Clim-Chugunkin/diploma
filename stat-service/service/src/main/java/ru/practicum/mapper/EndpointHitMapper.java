package ru.practicum.mapper;

import ru.practicum.DTO.EndpointHitDTO;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHitDTO toEndpointHitDTO(EndpointHit endpoint) {
        EndpointHitDTO endpointHitDTO = new EndpointHitDTO();
        endpointHitDTO.setId(endpoint.getId());
        endpointHitDTO.setApp(endpoint.getApp());
        endpointHitDTO.setUri(endpoint.getUri());
        endpointHitDTO.setIp(endpoint.getIp());
        endpointHitDTO.setTimestamp(endpoint.getTimestamp().format(formatter));
        return endpointHitDTO;
    }

    public static EndpointHit toEndpointHit(EndpointHitDTO endpointDTO) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(endpointDTO.getId());
        endpointHit.setApp(endpointDTO.getApp());
        endpointHit.setUri(endpointDTO.getUri());
        endpointHit.setIp(endpointDTO.getIp());
        endpointHit.setTimestamp(LocalDateTime.parse(endpointDTO.getTimestamp(), formatter));
        return endpointHit;
    }


}
