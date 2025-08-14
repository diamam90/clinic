package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.mapper.AppointmentMapper;
import com.diamam.appointmentservice.dto.appointment.AppointmentResponse;
import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.dto.appointment.GenerateSingleDayRequest;
import com.diamam.appointmentservice.service.AutoGenerateAppointmentService;
import com.diamam.appointmentservice.service.GenerateAppointmentService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import one.util.streamex.EntryStream;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager/appointments/generating")
public class ManagerController {

    private final GenerateAppointmentService generateAppointmentService;
    private final AutoGenerateAppointmentService autoGenerateAppointmentService;
    private final AppointmentMapper appointmentMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Map<LocalDate, List<AppointmentResponse>> generateForFewDays(@RequestBody @Valid GenerateRequest request) {
        var appointments = generateAppointmentService.generateByDoctorIdAndDaysCount(request);

        return EntryStream.of(appointments)
                .mapValues(values ->
                        values.stream()
                                .map(appointmentMapper::toDto)
                                .toList())
                .sorted(Map.Entry.comparingByKey())
                .toSortedMap();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bydate")
    public List<AppointmentResponse> generateForDate(@RequestBody @Valid GenerateSingleDayRequest request) {
        var appointments = generateAppointmentService.generateByDoctorId(request);

        return appointments.stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/auto")
    public void autogenerate(){
        autoGenerateAppointmentService.generateSchedule();
    }
}
