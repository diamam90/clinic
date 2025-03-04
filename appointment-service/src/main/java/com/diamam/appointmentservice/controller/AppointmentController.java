package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.controller.swagger.AppointmentControllerSwagger;
import com.diamam.appointmentservice.mapper.AppointmentMapper;
import com.diamam.appointmentservice.model.AppointmentResponse;
import com.diamam.appointmentservice.model.CreateAppointmentRequest;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController implements AppointmentControllerSwagger {

    private final AppointmentService appointmentService;
    private final AppointmentMapper mapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@RequestBody CreateAppointmentRequest request) {
        return mapper.toDto(appointmentService.save(request));
    }

    @Override
    @GetMapping("/{appointmentId}")
    public AppointmentResponse findById(@PathVariable(name = "appointmentId") Long appointmentId) {
        return mapper.toDto(appointmentService.findById(appointmentId));
    }

    @Override
    @GetMapping("/doctor")
    public List<AppointmentResponse> findByDoctorIdAndDate(@RequestParam(name = "doctorId") String doctorId,
                                                           @RequestParam(name = "date") LocalDate date) {
        return appointmentService.findByDoctorIdAndDate(doctorId, date).stream().map(mapper::toDto).toList();
    }

    @Override
    @GetMapping("/client")
    public List<AppointmentResponse> findByClientId(@RequestParam(name = "clientId") String clientId) {
        return appointmentService.findByClientId(clientId).stream().map(mapper::toDto).toList();
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{appointmentId}")
    public Long cancel(@PathVariable(name = "appointmentId") Long appointmentId) {
        return appointmentService.cancel(appointmentId);
    }
}
