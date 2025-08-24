package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Valid
@Schema(description = "Запрос на создание расписание врача")
public record GenerateSingleDayRequest(

        @NotBlank(message = "doctorId must not be blank")
        @Schema(description = "Идентификатор врача", requiredMode = Schema.RequiredMode.REQUIRED)
        String doctorId,

        @Schema(description = "Длительность приема врача",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                defaultValue = "30 min")
        Duration duration,

        @NotNull(message = "date must not be null")
        @Schema(description = "Дата, на которую необходимо сгенерировать расписание",
                requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate date,

        @Schema(description = "Начало смены у врача",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                defaultValue = "8:00:00")
        LocalTime shiftStart,

        @Schema(description = "Окончание смены у врача",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                defaultValue = "16:00:00")
        LocalTime shiftEnd
) {

    public GenerateSingleDayRequest {
        if (Objects.isNull(duration)) duration = Duration.ofMinutes(30);
        if (Objects.isNull(shiftStart)) shiftStart = LocalTime.of(8, 0, 0);
        if (Objects.isNull(shiftEnd)) shiftEnd = LocalTime.of(16, 0, 0);
    }
}
