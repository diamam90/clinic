package com.diamam.clinic.model.doctor;

import com.diamam.clinic.entity.Doctor;

import java.util.Objects;

public record DoctorInfo(
        String id,
        String firstName,
        String middleName,
        String lastName,
        String speciality,
        String description) {

    public DoctorInfo {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        Objects.requireNonNull(speciality);
    }

    public DoctorInfo(String id, String firstName, String lastName, String speciality) {
        this(id, firstName, null, lastName, speciality, null);
    }

    public String fullName() {
        return String.join(" ", lastName, firstName, middleName);
    }

    public static DoctorInfo toDto(Doctor doctor) {
        return new DoctorInfo(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getMiddleName(),
                doctor.getLastName(),
                doctor.getSpeciality(),
                doctor.getDescription());
    }
}
