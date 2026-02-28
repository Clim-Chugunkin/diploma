package ru.practicum.ewm.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stats.client.StatsClient;

@Configuration
public class MainServerConfig {

    @Value("${STATS_SERVER_URL}")
    private String statsServerUrl;// = "http://localhost:8080";

    @Bean
    public StatsClient statsClient(RestTemplateBuilder builder) {
        return new StatsClient(statsServerUrl, builder);
    }
}
