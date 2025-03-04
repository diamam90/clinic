package com.diamam.clientservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Клиент")
public record ClientResponse(
        @Schema(description = "Идентификатор клиента")
        String id,
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Отчество")
        String middleName,
        @Schema(description = "Возраст")
        Integer age) {
}
