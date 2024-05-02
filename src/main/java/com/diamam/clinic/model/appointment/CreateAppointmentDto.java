package com.diamam.clinic.model.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentDto(
        @NotNull(message = "Идентификатор не указан")
        @NotBlank(message = "Идентификатор не должен быть пустым")
        String id,
        @NotNull(message = "Идентификатор клиента не указан")
        @NotBlank(message = "Идентификатор клиента не должен быть пустым")
        String clientId,
        @NotNull(message = "Идентификатор врача не указан")
        @NotBlank(message = "Идентификатор врача не должен быть пустым")
        String doctorId
) {
}

