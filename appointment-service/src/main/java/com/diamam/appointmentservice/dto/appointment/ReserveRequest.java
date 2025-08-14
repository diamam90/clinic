package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Valid
@Schema(description = "Запрос на создание приема")
public record ReserveRequest(

        @NotNull(message = "clientId must not be null")
        @NotBlank(message = "clientId must not be blank")
        @Schema(description = "Идентификатор клиента")
        String clientId
) {
}
