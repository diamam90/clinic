package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.controller.swagger.AppointmentControllerSwagger;
import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.dto.appointment.AppointmentResponse;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;
import com.diamam.appointmentservice.mapper.AppointmentMapper;
import com.diamam.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController implements AppointmentControllerSwagger {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping("/{appointmentId}/reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse reserve(
            @PathVariable(name = "appointmentId") Long appointmentId,
            @Valid
            @RequestBody ReserveRequest request) {
        return appointmentMapper.toDto(appointmentService.reserve(appointmentId, request));
    }

    @PostMapping("/{appointmentId}/cancelling")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse cancel(@PathVariable(name = "appointmentId") Long appointmentId) {
        return appointmentMapper.toDto(appointmentService.cancel(appointmentId));
    }

    @GetMapping("/{appointmentId}")
    public AppointmentResponse findById(@PathVariable(name = "appointmentId") Long appointmentId) {
        return appointmentMapper.toDto(appointmentService.findById(appointmentId));
    }

    @GetMapping("/filtration")
    public List<AppointmentResponse> filter(AppointmentFilter filter) {
        var appointments = appointmentService.find(filter);
        return StreamEx.of(appointments).map(appointmentMapper::toDto).toList();
    }
}
