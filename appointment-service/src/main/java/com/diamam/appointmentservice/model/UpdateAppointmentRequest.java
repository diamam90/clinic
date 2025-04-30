package com.diamam.appointmentservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateAppointmentRequest(

        @Schema(description = "Идентификатор клиента")
        String clientId,

        @Schema(description = "Идентификатор доктора")
        String doctorId
) {
}
