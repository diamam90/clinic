package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.model.doctor.CreateDoctorDto;
import com.diamam.clinic.model.doctor.UpdateDoctorDto;
import com.diamam.clinic.repository.DoctorRepository;
import com.diamam.clinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public Doctor save(CreateDoctorDto doctorDto) {
        Doctor doctor = new Doctor(
                doctorDto.firstName(),
                doctorDto.middleName(),
                doctorDto.lastName(),
                doctorDto.speciality(),
                doctorDto.description(),
                doctorDto.status(),
                doctorDto.rating()
        );

        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor update(UpdateDoctorDto doctorDto) {
        doctorRepository.findById(doctorDto.id()).orElseThrow();

        Doctor doctor = new Doctor(
                doctorDto.id(),
                doctorDto.firstName(),
                doctorDto.middleName(),
                doctorDto.lastName(),
                doctorDto.speciality(),
                doctorDto.description(),
                doctorDto.status(),
                doctorDto.rating()
        );

        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(String id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        byId.ifPresent(doctorRepository::delete);
    }

    @Override
    public Optional<Doctor> findById(String id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
