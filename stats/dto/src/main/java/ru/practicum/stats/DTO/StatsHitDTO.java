package ru.practicum.stats.DTO;

import lombok.Data;

@Data
public class StatsHitDTO {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
