package com.diamam.clinic.model;

public record ClientInfo(
        long id,
        String firstName,
        String middleName,
        String lastName,
        Byte age
) {
}
