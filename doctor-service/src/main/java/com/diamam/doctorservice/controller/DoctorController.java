package com.diamam.doctorservice.controller;

import com.diamam.doctorservice.controller.swagger.DoctorControllerSwagger;
import com.diamam.doctorservice.mapper.DoctorMapper;
import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.DoctorResponse;
import com.diamam.doctorservice.model.PatchDoctorRequest;
import com.diamam.doctorservice.model.UpdateDoctorRequest;
import com.diamam.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController implements DoctorControllerSwagger {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    private final BeanNameUrlHandlerMapping beanNameHandlerMapping;

    @GetMapping()
    public DoctorResponse getById(@RequestParam(name = "doctorId") String doctorId) {
        return doctorMapper.toDto(doctorService.findById(doctorId));
    }

    @PostMapping
    public DoctorResponse create(@RequestBody CreateDoctorRequest request) {
        return doctorMapper.toDto(doctorService.create(request));
    }

    @PostMapping("/{doctorId}")
    public DoctorResponse update(@PathVariable String doctorId, @RequestBody UpdateDoctorRequest request) {
        return doctorMapper.toDto(doctorService.update(doctorId, request));
    }

    @PatchMapping("/{doctorId}")
    public DoctorResponse patch(@PathVariable String doctorId, @RequestBody PatchDoctorRequest request) {
        return doctorMapper.toDto(doctorService.patch(doctorId, request));
    }

    @DeleteMapping("/{doctorId}")
    public void delete(@PathVariable String doctorId) {
        doctorService.deleteById(doctorId);
    }

    @GetMapping("/find")
    public List<DoctorResponse> findBySpeciality(@RequestParam(value = "speciality") String speciality) {
        return doctorService.findBySpeciality(speciality).stream().map(doctorMapper::toDto).toList();
    }
}
