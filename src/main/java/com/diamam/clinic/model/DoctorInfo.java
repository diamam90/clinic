package com.diamam.clinic.model;

import java.util.Objects;

public record DoctorInfo(
        long id,
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

    public DoctorInfo(Long id,String firstName, String lastName, String speciality) {
        this(id,firstName, null, lastName, speciality, null);
    }

    public String fullName() {
        return String.join(" ", lastName, firstName, middleName);
    }
}
