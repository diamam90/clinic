package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.controller.swagger.ScheduleControllerSwagger;
import com.diamam.appointmentservice.dto.appointment.ScheduleResponse;
import com.diamam.appointmentservice.mapper.AppointmentScheduleMapper;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController implements ScheduleControllerSwagger {

    private final AppointmentService appointmentService;
    private final AppointmentScheduleMapper appointmentScheduleMapper;

    @GetMapping("/{doctorId}")
    public List<ScheduleResponse> findByDoctorIdAndDateBetween(@PathVariable(name = "doctorId") String doctorId,
                                                               @RequestParam(required = false) LocalDate start,
                                                               @RequestParam(required = false) LocalDate end
    ) {
        var appointments = appointmentService.findByDoctorIdAndDateBetween(doctorId, start, end);

        return appointmentScheduleMapper.toDto(appointments);
    }
}
