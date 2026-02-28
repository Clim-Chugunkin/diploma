package ru.practicum.ewm.server.compilation.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class NewCompilationDto {
    private Set<Long> events;
    private Boolean pinned = false;
    @NotNull(message = "не указан заголовок")
    @NotBlank(message = "заголовок не может быть пустым")
    @Size(min = 1, max = 50, message = "title должно быть от 1 до 50 символов")
    private String title;
}
