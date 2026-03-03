package ru.practicum.ewm.server.category.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    @NotNull(message = "не указано имя")
    @NotBlank(message = "имя не должно быть пустым")
    @Size(max = 50, message = "имя категории должно быть  не больше 50 символов")
    private String name;
}
