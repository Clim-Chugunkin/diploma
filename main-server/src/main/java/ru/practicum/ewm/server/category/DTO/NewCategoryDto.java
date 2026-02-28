package ru.practicum.ewm.server.category.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewCategoryDto {
    @NotNull(message = "не указано имя")
    @NotEmpty(message = "имя не должно быть пустым")
    private String name;
}
