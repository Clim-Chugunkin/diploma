package ru.practicum.ewm.server.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotNull(message = "не указана почта")
    @Email(message = "почта указана неправильно")
    @NotBlank(message = "почта не может быть пустой")
    @Size(min = 6, max = 254, message = "емаил должен быть от 6 до 254 символов")
    private String email;
    @NotNull(message = "не указано имя")
    @NotBlank(message = "имя не может быть пустым")
    @Size(min = 2, max = 250, message = "Имя должно быть от 2 до 250 символов")
    private String name;
}
