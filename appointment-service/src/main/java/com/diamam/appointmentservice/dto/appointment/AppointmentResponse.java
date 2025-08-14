package com.diamam.appointmentservice.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        String clientId,
        String doctorId,
        LocalTime start,
        LocalTime end,
        LocalDate date,
        boolean isAvailable
) {
}
