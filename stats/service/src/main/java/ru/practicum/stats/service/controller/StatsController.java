package ru.practicum.stats.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.DTO.StatsHitDTO;
import ru.practicum.stats.DTO.ViewStats;
import ru.practicum.stats.service.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public StatsHitDTO addEndpoint(@RequestBody StatsHitDTO endpoint) {
        return statsService.addEndpoint(endpoint);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStat(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) String[] uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        if (uris != null) return statsService.getStatistic(start, end, uris, unique);
        return statsService.getStatistic(start, end, unique);
    }
}
