package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.DTO.ViewStats;

import java.util.List;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private final String serverUrl;

    public StatsClient(String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.serverUrl = serverUrl;
    }

    public StatsHitDTO addEndpoint(StatsHitDTO endpoint) {
        StatsHitDTO endpointHitDTO = objectMapper.convertValue(post(serverUrl + "/hit", endpoint).getBody(), StatsHitDTO.class);
        log.info("был обработан запрос к эндпоинту {} ", endpointHitDTO.getApp());
        return endpointHitDTO;
    }

    public List<ViewStats> getStatistic(String start,
                                        String end,
                                        String[] uris,
                                        boolean unique) {
        String baseUrl = serverUrl + "/stats";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);
        if (uris != null) {
            builder.queryParam("uris", uris);
        }
        Object body = get(builder.build().toUriString()).getBody();
        return ((List<?>) body).stream()
                .map(o -> objectMapper.convertValue(o, ViewStats.class)
                )
                .toList();
    }
}
