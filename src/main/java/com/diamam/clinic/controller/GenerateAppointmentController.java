package com.diamam.clinic.controller;

import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.GenerateRequestDto;
import com.diamam.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments/generate")
@RequiredArgsConstructor
public class GenerateAppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping()
    public ResponseEntity<List<AppointmentInfo>> generateSchedule(@RequestBody GenerateRequestDto dto) {
        return new ResponseEntity<>(appointmentService.generateSchedule(dto.doctorId(), dto.startDate(), dto.days()), HttpStatus.CREATED);
    }
}
