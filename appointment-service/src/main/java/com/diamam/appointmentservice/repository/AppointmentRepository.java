package com.diamam.appointmentservice.repository;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Long> {
    List<AppointmentEntity> findByClientId(String clientId);
    List<AppointmentEntity> findByDoctorId(String doctorId);
    List<AppointmentEntity> findByDoctorIdAndDate(String doctorId, LocalDate date);
}
