package com.diamam.appointmentservice.service.impl;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.mapper.AppointmentMapper;
import com.diamam.appointmentservice.model.CreateAppointmentRequest;
import com.diamam.appointmentservice.model.UpdateAppointmentRequest;
import com.diamam.appointmentservice.repository.AppointmentRepository;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper mapper;

    @Override
    @Transactional
    public AppointmentEntity save(CreateAppointmentRequest request) {
        AppointmentEntity appointmentEntity = mapper.create(request);
        appointmentEntity.setAvailable(false);
        return appointmentRepository.save(appointmentEntity);
    }

    @Override
    @Transactional
    public AppointmentEntity update(Long appointmentId, UpdateAppointmentRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        appointment.setClientId(request.clientId());
        appointment.setDoctorId(request.doctorId());
        return appointment;
    }

    @Override
    public AppointmentEntity findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow();
    }

    @Override
    public List<AppointmentEntity> findByDoctorIdAndDate(String doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }

    @Override
    public List<AppointmentEntity> findByClientId(String clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    @Override
    @Transactional
    public Long cancel(Long appointmentId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId).orElseThrow();
        appointmentEntity.setClientId(null);
        appointmentEntity.setAvailable(true);
        return appointmentId;
    }
}
