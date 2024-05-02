package com.diamam.clinic.stub;

import com.diamam.clinic.entity.Doctor;

public class DoctorStubs {

    public static Doctor ivanIvanov() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Ivan");
        doctor.setDescription("this is description for the doctor #1");
        doctor.setLastName("Ivanov");
        doctor.setRating(1.0);
        doctor.setSpeciality("speciality #1");
        doctor.setStatus("status");
        doctor.setMiddleName("Ivanovich");
        return doctor;
    }

    public static Doctor peterDmitriev() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Peter");
        doctor.setDescription("this is description for the doctor #2");
        doctor.setLastName("Dmitriev");
        doctor.setRating(1.3);
        doctor.setSpeciality("speciality #2");
        doctor.setStatus("active");
        return doctor;
    }

    public static Doctor savedIvanIvanov() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Ivan");
        doctor.setDescription("this is description for the doctor #1");
        doctor.setLastName("Ivanov");
        doctor.setRating(1.0);
        doctor.setSpeciality("speciality #1");
        doctor.setStatus("status");
        doctor.setMiddleName("Ivanovich");
        doctor.setId("1");
        return doctor;
    }

    public static Doctor savedPeterDmitriev() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Peter");
        doctor.setDescription("this is description for the doctor #2");
        doctor.setLastName("Dmitriev");
        doctor.setRating(1.3);
        doctor.setSpeciality("speciality #2");
        doctor.setStatus("active");
        doctor.setId("2");
        return doctor;
    }
}
