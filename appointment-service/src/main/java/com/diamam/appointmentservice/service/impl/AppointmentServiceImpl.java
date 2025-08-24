package com.diamam.appointmentservice.service.impl;

import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.exception.BadRequestException;
import com.diamam.appointmentservice.repository.AppointmentRepository;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final Clock clock;

    @Value("${appointment-service.schedule.number-of-days:28}")
    private Integer NUMBER_OF_DAYS;

    @Override
    public AppointmentEntity reserve(Long appointmentId, ReserveRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(badRequest(appointmentId));

        appointment.setClientId(request.clientId());
        appointment.setAvailable(false);
        return appointment;
    }

    @Override
    public AppointmentEntity cancel(Long appointmentId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId)
                .orElseThrow(badRequest(appointmentId));

        appointmentEntity.setClientId(null);
        appointmentEntity.setAvailable(true);
        return appointmentEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentEntity findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(badRequest(appointmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentEntity> find(AppointmentFilter filter) {
        if (Objects.isNull(filter)) {
            return List.of();
        }

        if (Objects.nonNull(filter.clientId())) {
            if (Objects.nonNull(filter.date())) {
                return appointmentRepository.findByClientIdAndDate(filter.clientId(), filter.date());
            } else {
                return appointmentRepository.findByClientId(filter.clientId());
            }
        }

        if (Objects.nonNull(filter.doctorId())) {
            if (Objects.nonNull(filter.date())) {
                return appointmentRepository.findByDoctorIdAndDate(filter.doctorId(), filter.date());
            } else {
                return appointmentRepository.findByDoctorId(filter.doctorId());
            }
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentEntity> findByDoctorIdAndDateBetween(String doctorId, LocalDate start, LocalDate end) {
        var startDate = start == null ? LocalDate.now(clock) : start;
        var endDate = end == null ? startDate.plusDays(NUMBER_OF_DAYS) : end;
        return appointmentRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentEntity> findByClientId(String clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    private Supplier<BadRequestException> badRequest(Long appointmentId) {
        return () -> {
            throw new BadRequestException("Appointment with id %d not found".formatted(appointmentId));
        };
    }
}
