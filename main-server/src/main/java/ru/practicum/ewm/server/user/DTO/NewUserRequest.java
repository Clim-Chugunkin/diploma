package ru.practicum.ewm.server.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotNull(message = "не указана почта")
    @Email(message = "почта указана неправильно")
    private String email;
    @NotNull(message = "не указано имя")
    private String name;
}
