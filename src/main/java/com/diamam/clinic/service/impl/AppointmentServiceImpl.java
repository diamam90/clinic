package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Appointment;
import com.diamam.clinic.entity.Client;
import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.exception.NotFoundException;
import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.UpdateAppointmentDto;
import com.diamam.clinic.repository.AppointmentRepository;
import com.diamam.clinic.repository.ClientRepository;
import com.diamam.clinic.repository.DoctorRepository;
import com.diamam.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ClientRepository clientRepository;

    @Override
    public String make(UpdateAppointmentDto dto) {
        Appointment appointment = appointmentRepository
                .findById(dto.id())
                .orElseThrow(()-> NotFoundException.AppointmentNotFoundException.byId(dto.id()));
        Client client = clientRepository.findById(dto.clientId()).orElseThrow(
                ()-> NotFoundException.ClientNotFoundException.byId(dto.clientId())
        );
        appointment.setClient(client);
        return appointmentRepository.save(appointment).getId();
    }

    @Override
    public String deleteById(String id) {
        try {
            appointmentRepository.deleteById(id);
            return id;
        } catch (IllegalArgumentException e) {
            throw NotFoundException.AppointmentNotFoundException.byId(id);
        }
    }

    @Override
    public AppointmentInfo getById(String id) {
        return appointmentRepository.
                findById(id).
                map(AppointmentInfo::toDto).
                orElseThrow(()-> NotFoundException.AppointmentNotFoundException.byId(id));
    }

    @Override
    public List<AppointmentInfo> getByClientId(String clientId) {
        return appointmentRepository
                .findByClientId(clientId)
                .stream()
                .map(AppointmentInfo::toDto)
                .toList();
    }

    @Override
    public List<AppointmentInfo> getFreeSpotByDoctorIdAndDate(String doctorId, LocalDate date) {
        return appointmentRepository
                .findByDateAndDoctorIdAndClientIsNull(date, doctorId)
                .stream()
                .map(AppointmentInfo::toDto)
                .toList();
    }

    @Override
    public List<AppointmentInfo> generateSchedule(String doctorId, LocalDate startDate, Integer days) {
        Doctor doctor = doctorRepository
                .findById(doctorId)
                .orElseThrow(() ->
                        NotFoundException.DoctorNotFoundException.byId(doctorId));

        List<Appointment> appointmentList = generateAppointments(doctor, startDate, days != null ? days : 1);

        return appointmentRepository
                .saveAll(appointmentList)
                .stream()
                .map(AppointmentInfo::toDto)
                .toList();
    }

    @Override
    public List<AppointmentInfo> generateScheduleOneDay(String doctorId, LocalDate startDate) {
        return generateSchedule(doctorId, startDate, 1);
    }

    protected List<Appointment> generateAppointments(Doctor doctor, LocalDate startDate, int days) {
        return IntStream.range(0, days).mapToObj(day ->
                        IntStream
                                .range(0, 18)
                                .mapToObj(i -> new Appointment(doctor, startDate.plusDays(day),
                                        LocalTime.of(8, 0).plusMinutes(30L * i),
                                        LocalTime.of(8, 30).plusMinutes(30L * i))
                                ))
                .flatMap(Function.identity())
                .toList();
    }
}

