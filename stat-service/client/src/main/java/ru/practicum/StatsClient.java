package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.DTO.EndpointHitDTO;
import ru.practicum.DTO.ViewStats;

import java.util.List;

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

    public EndpointHitDTO addEndpoint(EndpointHitDTO endpoint) {

        return objectMapper.convertValue(post(serverUrl + "/hit", endpoint).getBody(), EndpointHitDTO.class);
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
