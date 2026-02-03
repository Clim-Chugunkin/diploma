package ru.practicum.DTO;

import lombok.Data;

@Data
public class EndpointHitDTO {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
