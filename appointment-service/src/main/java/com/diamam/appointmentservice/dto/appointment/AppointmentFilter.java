package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Параметры фильтрации приемов")
public record AppointmentFilter(

        @Schema(description = "Идентификатор врача")
        String doctorId,

        @Schema(description = "Идентификатор пациента")
        String clientId,

        @Schema(description = "Дата приема")
        LocalDate date
) {
}
