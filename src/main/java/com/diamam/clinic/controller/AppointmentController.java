package com.diamam.clinic.controller;

import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.GenerateRequestDto;
import com.diamam.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentInfo> getById(@PathVariable("appointmentId") String appointmentId){
        return new ResponseEntity<>(appointmentService.getById(appointmentId),HttpStatus.OK);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentInfo>> getFreeSpotsToday(@PathVariable("doctorId") String doctorId) {
        return new ResponseEntity<>(
                appointmentService.getFreeSpotByDoctorIdAndDate(doctorId, LocalDate.now()), HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentInfo>> getByClientId(@PathVariable("clientId") String clientId) {
        return new ResponseEntity<>(appointmentService
                .getByClientId(clientId), HttpStatus.OK);
    }

}
