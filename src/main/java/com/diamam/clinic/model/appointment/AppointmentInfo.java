package com.diamam.clinic.model.appointment;

import com.diamam.clinic.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentInfo(
        String id,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        AppointmentDoctor doctor,
        AppointmentClient client,
        boolean freeSpot
) {

    public static AppointmentInfo toDto(Appointment appointment) {
        return new AppointmentInfo(
                appointment.getId(),
                appointment.getDate(),
                appointment.getStart(),
                appointment.getEnd(),
                AppointmentDoctor.forAppointment(appointment.getDoctor()),
                appointment.getClient() != null ? AppointmentClient.forAppointment(appointment.getClient()) : null,
                appointment.getClient() == null);
    }
}
