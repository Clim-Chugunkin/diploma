package ru.practicum.ewm.server.compilation.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class NewCompilationDto {
    private Set<Long> events;
    @NotNull(message = "не указан pinned")
    private Boolean pinned;
    @NotNull(message = "не указан заголовок")
    @NotBlank(message = "заголовок не может быть пустым")
    private String title;
}
