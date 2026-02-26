package ru.practicum.stats.service.service;

import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.DTO.ViewStats;

import java.util.List;

public interface StatsService {
    StatsHitDTO addEndpoint(StatsHitDTO endpoint);

    List<ViewStats> getStatistic(String start, String stop, boolean unique);

    List<ViewStats> getStatistic(String start, String stop, String[] uris, boolean unique);
}
