package com.diamam.appointmentservice.repository;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByClientId(String clientId);

    List<AppointmentEntity> findByClientIdAndDate(String clientId, LocalDate date);

    List<AppointmentEntity> findByDoctorId(String doctorId);

    List<AppointmentEntity> findByDoctorIdAndDate(String doctorId, LocalDate date);

    List<AppointmentEntity> findByDoctorIdAndDateBetween(String doctorId, LocalDate start, LocalDate end);

    @Query("""
                SELECT COUNT(a)>0 FROM AppointmentEntity a
                WHERE a.doctorId = :doctorId
                    AND a.date = :date
                    AND a.isAvailable = true
            """)
    boolean existReservedByDoctorAndDate(String doctorId, LocalDate date);

    @Query("""
                SELECT COUNT(a)>0 FROM AppointmentEntity a
                WHERE a.doctorId = :doctorId
                    AND a.date BETWEEN :start AND :end
                    AND a.isAvailable = true
            """)
    boolean existReservedByDoctorAndDateBetween(String doctorId, LocalDate start, LocalDate end);
}
