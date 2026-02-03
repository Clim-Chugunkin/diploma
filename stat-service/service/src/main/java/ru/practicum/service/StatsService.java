package ru.practicum.service;

import ru.practicum.DTO.EndpointHitDTO;
import ru.practicum.DTO.ViewStats;

import java.util.List;

public interface StatsService {
    EndpointHitDTO addEndpoint(EndpointHitDTO endpoint);

    List<ViewStats> getStatistic(String start, String stop, boolean unique);

    List<ViewStats> getStatistic(String start, String stop, String[] uris, boolean unique);

}
