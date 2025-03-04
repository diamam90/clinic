package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.model.CreateAppointmentRequest;
import com.diamam.appointmentservice.model.UpdateAppointmentRequest;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentEntity save(CreateAppointmentRequest request);

    AppointmentEntity update(UpdateAppointmentRequest request);

    AppointmentEntity findById(Long appointmentId);

    List<AppointmentEntity> findByDoctorIdAndDate(String doctorId, LocalDate date);

    List<AppointmentEntity> findByClientId(String clientId);

    Long cancel(Long appointmentId);
}
