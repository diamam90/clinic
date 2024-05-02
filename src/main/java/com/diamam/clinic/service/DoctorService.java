package com.diamam.clinic.service;

import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.model.doctor.CreateDoctorDto;
import com.diamam.clinic.model.doctor.UpdateDoctorDto;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor save(CreateDoctorDto doctorDto);

    Doctor update(UpdateDoctorDto doctorDto);

    void delete(String id);

    Optional<Doctor> findById(String id);

    List<Doctor> findAll();

}

