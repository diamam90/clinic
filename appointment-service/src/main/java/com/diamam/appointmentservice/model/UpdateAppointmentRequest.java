package com.diamam.appointmentservice.model;

public record UpdateAppointmentRequest(
        Long id,
        String clientId,
        String doctorId
) {
}
