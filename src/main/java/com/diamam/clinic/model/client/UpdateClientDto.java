package com.diamam.clinic.model.client;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateClientDto(
        @NotNull(message = "Идентификатор клиента не указан")
        String id,
        @NotNull(message = "Имя не указано")
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 20, message = "Имя должно быть длиной от {min} до {max} символов")
        String firstName,
        @Size(min = 2, max = 20, message = "Отчество должно быть длиной от {min} до {max} символов")
        String middleName,
        @NotNull(message = "Фамилия не указана")
        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(min = 2, max = 20, message = "Фамилия должна быть длиной от {min} до {max} символов")
        String lastName,
        @NotNull(message = "Возраст не указан")
        @Min(value = 1, message = "Возраст должен быть больше 1")
        @Max(value = 100, message = "Возраст должен быть меньше 100")
        Integer age,
        LocalDate birthDate,
        @NotNull(message = "Почта не указана")
        @NotBlank(message = "Электронная почта не может быть пустой")
        @Email(message = "Электронная почта не соответствует шаблону")
        String email,
        @NotNull(message = "Телефон не указан")
        @NotBlank(message = "Телефон не может быть пустым")
        String phone,
        @NotNull(message = "Паспорт не указан")
        @NotBlank(message = "Паспорт не может быть пустой")
        String passport
) {
}
