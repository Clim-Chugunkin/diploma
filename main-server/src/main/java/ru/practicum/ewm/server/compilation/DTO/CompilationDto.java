package ru.practicum.ewm.server.compilation.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.server.event.DTO.EventShortDto;

import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
