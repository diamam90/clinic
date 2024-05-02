package com.diamam.clinic.model.appointment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record GenerateRequestDto(
        @NotNull(message = "Идентификатор врача не указан")
        @NotBlank(message = "Идентификатор врача не должен быть пустым")
        String doctorId,
        @NotNull
        LocalDate startDate,
        @Positive(message = "Количество дней не может быть отрицательным")
        @Max(value = 14, message = "Количество дней не может быть больше ${value}")
        Integer days
) {

    public GenerateRequestDto(String doctorId, LocalDate startDate) {
        this(doctorId, startDate, 1);
    }
}
