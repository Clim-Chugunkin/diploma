package ru.practicum.stats.service.mapper;

import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.service.model.StatsHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsHitMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static StatsHitDTO toEndpointHitDTO(StatsHit endpoint) {
        StatsHitDTO endpointHitDTO = new StatsHitDTO();
        endpointHitDTO.setId(endpoint.getId());
        endpointHitDTO.setApp(endpoint.getApp());
        endpointHitDTO.setUri(endpoint.getUri());
        endpointHitDTO.setIp(endpoint.getIp());
        endpointHitDTO.setTimestamp(endpoint.getTimestamp().format(formatter));
        return endpointHitDTO;
    }

    public static StatsHit toEndpointHit(StatsHitDTO endpointDTO) {
        StatsHit endpointHit = new StatsHit();
        endpointHit.setId(endpointDTO.getId());
        endpointHit.setApp(endpointDTO.getApp());
        endpointHit.setUri(endpointDTO.getUri());
        endpointHit.setIp(endpointDTO.getIp());
        endpointHit.setTimestamp(LocalDateTime.parse(endpointDTO.getTimestamp(), formatter));
        return endpointHit;
    }
}
