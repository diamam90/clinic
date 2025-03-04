package com.diamam.doctorservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление доктора")
public record UpdateDoctorRequest(
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Отчество")
        String middleName,
        @Schema(description = "Специальность")
        String speciality,
        @Schema(description = "Должность")
        String title,
        @Schema(description = "Описание")
        String description) {
}
