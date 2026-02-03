package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.EndpointHitDTO;
import ru.practicum.DTO.ViewStats;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.EndpointRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointRepository endpointRepository;

    @Override
    public EndpointHitDTO addEndpoint(EndpointHitDTO endpoint) {
        return EndpointHitMapper.toEndpointHitDTO(endpointRepository.save(EndpointHitMapper.toEndpointHit(endpoint)));
    }

    @Override
    public List<ViewStats> getStatistic(String start, String stop, boolean unique) {
        LocalDateTime dateStart = LocalDateTime.parse(start, EndpointHitMapper.formatter);
        LocalDateTime dateStop = LocalDateTime.parse(stop, EndpointHitMapper.formatter);

        return endpointRepository.getStatistic(dateStart, dateStop, unique);
    }

    @Override
    public List<ViewStats> getStatistic(String start, String stop, String[] uris, boolean unique) {
        LocalDateTime dateStart = LocalDateTime.parse(start, EndpointHitMapper.formatter);
        LocalDateTime dateStop = LocalDateTime.parse(stop, EndpointHitMapper.formatter);

        return endpointRepository.getStatistic(dateStart, dateStop, uris, unique);
    }

}
