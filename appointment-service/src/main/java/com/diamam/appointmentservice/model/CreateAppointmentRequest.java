package com.diamam.appointmentservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "Запрос на создание приема")
public record CreateAppointmentRequest(

        @Schema(description = "Идентификатор клиента")
        String clientId,

        @Schema(description = "Идентификатор доктора")
        String doctorId,

        @Schema(description = "Начало приема")
        LocalTime start,

        @Schema(description = "Окончание приема")
        LocalTime end,

        @Schema(description = "Дата приема")
        LocalDate date
) {
}
