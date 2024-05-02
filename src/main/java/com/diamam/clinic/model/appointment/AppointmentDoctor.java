package com.diamam.clinic.model.appointment;

import com.diamam.clinic.entity.Doctor;

public record AppointmentDoctor(String id, String fullName) {
    public static AppointmentDoctor forAppointment(Doctor doctor) {
        return new AppointmentDoctor(
                doctor.getId(),
                String.join(" ", doctor.getLastName(), doctor.getFirstName(), doctor.getMiddleName()));
    }
}
