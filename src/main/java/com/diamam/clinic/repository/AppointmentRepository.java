package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    Optional<Appointment> findByDoctorIdAndClientIdAndDate(String doctorId,String clientId, LocalDate date);
    List<Appointment> findByClientId(String clientId);
    List<Appointment> findByDateAndDoctorIdAndClientIsNull(LocalDate date, String doctorId);
}
