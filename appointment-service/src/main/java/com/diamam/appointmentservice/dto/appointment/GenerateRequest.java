package com.diamam.appointmentservice.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Valid
@Schema(description = "Запрос на создание расписания врача на заданное количество дней")
public record GenerateRequest(

        @Schema(description = "Идентификатор врача", requiredMode = Schema.RequiredMode.REQUIRED)
        String doctorId,

        @Schema(description = "Длительность приема врача, по умолчанию 30 мин", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Duration duration,

        @Schema(description = "Первый день расписания", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate dateStart,

        @Schema(description = "Начало смены у врача", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalTime shiftStart,

        @Schema(description = "Окончание смены у врача", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalTime shiftEnd,

        @NotNull(message = "days must not be null")
        @Positive(message = "days must be positive")
        @Schema(description = "Количество дней, на которое нужно создать расписание", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
        Integer days
) {

    public GenerateRequest(String doctorId, LocalDate dateStart, Integer days) {
        this(doctorId, null, dateStart, null, null, days);
    }

    public GenerateRequest {
        if (Objects.isNull(duration)) duration = Duration.ofMinutes(30);
        if (Objects.isNull(shiftStart)) shiftStart = LocalTime.of(8, 0, 0);
        if (Objects.isNull(shiftEnd)) shiftEnd = LocalTime.of(16, 0, 0);
    }
}
