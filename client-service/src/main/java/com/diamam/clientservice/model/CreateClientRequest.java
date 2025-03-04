package com.diamam.clientservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание клиента")
public record CreateClientRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 3, max = 20, message = "Имя должно быть от 3 до 20 символов")
        @Schema(description = "Имя")
        String firstName,
        @Size(min = 1, max = 20, message = "Фамилия должна быть от 1 до 20 символов")
        @Schema(description = "Фамилия")
        String lastName,
        @Size(min = 3, max = 20, message = "Отчество должно быть от 3 до 20 символов")
        @Schema(description = "Отчество")
        String middleName
) {
}
