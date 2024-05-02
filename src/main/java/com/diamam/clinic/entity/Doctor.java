package com.diamam.clinic.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "doctor")
public class Doctor {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String speciality;
    private String description;
    private String status;
    private Double rating = 0.0;

    public Doctor(String id, String firstName, String middleName, String lastName, String speciality, String description, String status, Double rating) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.description = description;
        this.status = status;
        this.rating = rating;
    }

    public Doctor(String id, String firstName, String lastName, String speciality, String description) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.description = description;
    }

    public Doctor(String firstName, String middleName, String lastName, String speciality, String description, String status, Double rating) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.description = description;
        this.status = status;
        this.rating = rating;
    }
}
