package com.diamam.clinic.stub;

import com.diamam.clinic.entity.Appointment;
import com.diamam.clinic.entity.Client;
import com.diamam.clinic.entity.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentStubs {

    public static Appointment freeSpot_1() {
        Appointment appointment = new Appointment(LocalDate.of(2022, 1, 1), LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0));
        return appointment;
    }

    public static Appointment freeSpot_2() {
        Appointment appointment = new Appointment(LocalDate.of(2022, 1, 1), LocalTime.of(8, 30, 0), LocalTime.of(9, 0, 0));
        return appointment;
    }

    public static Appointment savedFreeSpot_1() {
        Doctor doctor = DoctorStubs.savedIvanIvanov();
        Appointment appointment = new Appointment(doctor, LocalDate.of(2022, 1, 1), LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0));
        appointment.setId("01");
        return appointment;
    }

    public static Appointment savedFreeSpot_2() {
        Doctor doctor = DoctorStubs.savedPeterDmitriev();
        Appointment appointment = new Appointment(doctor, LocalDate.of(2022, 1, 1), LocalTime.of(8, 30, 0), LocalTime.of(9, 0, 0));
        appointment.setId("02");
        return appointment;
    }

    public static Appointment appointment_1() {
        Doctor doctor = DoctorStubs.savedIvanIvanov();
        Client client = ClientStubs.savedBoris();
        Appointment appointment = new Appointment(client, doctor, LocalDate.of(2022, 1, 1), LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0));
        return appointment;
    }

    public static Appointment appointment_2() {
        Doctor doctor = DoctorStubs.savedPeterDmitriev();
        Client client = ClientStubs.savedAlex();
        Appointment appointment = new Appointment(client, doctor, LocalDate.of(2022, 1, 1), LocalTime.of(8, 30, 0), LocalTime.of(9, 0, 0));
        return appointment;
    }


}
