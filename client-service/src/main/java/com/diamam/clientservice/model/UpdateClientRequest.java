package com.diamam.clientservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление клиента")
public record UpdateClientRequest(
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Отчество")
        String middleName,
        @Schema(description = "Возраст")
        Integer age,
        @Schema(description = "Данные паспорта")
        String passport,
        @Schema(description = "Телефон")
        String phone,
        @Schema(description = "Электронная почта")
        String email
) {
}
