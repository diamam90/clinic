package com.diamam.appointmentservice.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentRequest(
        String clientId,
        String doctorId,
        LocalTime start,
        LocalTime end,
        LocalDate date
) {
}
