package ru.practicum.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.DTO.ViewStats;
import ru.practicum.stats.service.mapper.StatsHitMapper;
import ru.practicum.stats.service.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository endpointRepository;

    @Override
    public StatsHitDTO addEndpoint(StatsHitDTO endpoint) {
        return StatsHitMapper.toEndpointHitDTO(endpointRepository.save(StatsHitMapper.toEndpointHit(endpoint)));
    }

    @Override
    public List<ViewStats> getStatistic(String start, String stop, boolean unique) {
        LocalDateTime dateStart = LocalDateTime.parse(start, StatsHitMapper.formatter);
        LocalDateTime dateStop = LocalDateTime.parse(stop, StatsHitMapper.formatter);
        if (dateStart.isAfter(dateStop)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректные данные запроса");
        }
        return endpointRepository.getStatistic(dateStart, dateStop, unique);
    }

    @Override
    public List<ViewStats> getStatistic(String start, String stop, String[] uris, boolean unique) {
        LocalDateTime dateStart = LocalDateTime.parse(start, StatsHitMapper.formatter);
        LocalDateTime dateStop = LocalDateTime.parse(stop, StatsHitMapper.formatter);
        if (dateStart.isAfter(dateStop)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректные данные запроса");
        }
        return endpointRepository.getStatistic(dateStart, dateStop, uris, unique);
    }
}
