package com.diamam.clinic.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "appointment")
@Data
public class Appointment {
    private String id;
    @DBRef
    private Client client;
    @DBRef
    private Doctor doctor;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    public Appointment(Client client, Doctor doctor, LocalDate date, LocalTime start, LocalTime end) {
        this.client = client;
        this.doctor = doctor;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Appointment(Doctor doctor, LocalDate date, LocalTime start, LocalTime end) {
        this.doctor = doctor;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Appointment( LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Appointment() {
    }
}
