package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Расписание приемов врача по дням")
public record ScheduleResponse(

        @Schema(description = "Дата приемов")
        LocalDate date,

        @Schema(description = "Список приемов дня")
        List<AppointmentResponse> appointments,

        @Schema(description = "Наличие свободных приемов")
        boolean isAvailable
) {
}
