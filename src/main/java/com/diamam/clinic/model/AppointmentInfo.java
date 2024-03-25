package com.diamam.clinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentInfo(
        long id,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        DoctorInfo doctor,
        Long clientId,
        boolean freeSpot
) {
}
