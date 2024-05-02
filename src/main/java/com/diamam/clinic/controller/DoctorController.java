package com.diamam.clinic.controller;

import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.model.doctor.CreateDoctorDto;
import com.diamam.clinic.model.doctor.UpdateDoctorDto;
import com.diamam.clinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping()
    public List<Doctor> getAllDoctors(){
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id){
        return ResponseEntity.of(doctorService.findById(id));
    }
    @PostMapping()
    public String createDoctor(@Valid @RequestBody CreateDoctorDto doctorDto){
        Doctor save = doctorService.save(doctorDto);
        return save.getId();
    }

    @PutMapping()
    public String updateDoctor(@Valid @RequestBody UpdateDoctorDto doctorDto){
        Doctor save = doctorService.update(doctorDto);
        return save.getId();
    }
}
