package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Valid
@Schema(description = "Запрос на создание приема")
public record ReserveRequest(

        @NotBlank(message = "clientId must not be blank")
        @Schema(description = "Идентификатор клиента")
        String clientId
) {
}
