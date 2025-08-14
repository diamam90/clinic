package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentEntity reserve(Long appointmentId, ReserveRequest request);

    AppointmentEntity cancel(Long appointmentId);

    AppointmentEntity findById(Long appointmentId);

    List<AppointmentEntity> find(AppointmentFilter filter);

    List<AppointmentEntity> findByDoctorIdAndDateBetween(String doctorId, LocalDate start, LocalDate end);

    List<AppointmentEntity> findByClientId(String clientId);


}