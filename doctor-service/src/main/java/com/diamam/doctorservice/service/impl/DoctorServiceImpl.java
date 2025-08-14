package com.diamam.doctorservice.service.impl;

import com.diamam.doctorservice.entity.DoctorEntity;
import com.diamam.doctorservice.mapper.DoctorMapper;
import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.PatchDoctorRequest;
import com.diamam.doctorservice.model.UpdateDoctorRequest;
import com.diamam.doctorservice.repository.DoctorRepository;
import com.diamam.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorEntity create(CreateDoctorRequest request) {
        var doctor = doctorMapper.fromCreateRequest(request);
        return doctorRepository.save(doctor);
    }

    @Override
    public DoctorEntity update(String doctorId, UpdateDoctorRequest request) {
        var doctor = doctorRepository.findById(doctorId).orElseThrow();
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setMiddleName(request.middleName());
        doctor.setTitle(request.title());
        doctor.setSpeciality(request.speciality());
        doctor.setDescription(request.description());
        return doctorRepository.save(doctor);
    }

    @Override
    public DoctorEntity findById(String id) {
        return doctorRepository.findById(id).orElseThrow();
    }

    @Override
    public DoctorEntity patch(String doctorId, PatchDoctorRequest request) {
        var doctor = doctorRepository.findById(doctorId).orElseThrow();
        Optional.ofNullable(request.firstName()).ifPresent(doctor::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(doctor::setLastName);
        Optional.ofNullable(request.middleName()).ifPresent(doctor::setMiddleName);
        Optional.ofNullable(request.speciality()).ifPresent(doctor::setSpeciality);
        Optional.ofNullable(request.title()).ifPresent(doctor::setTitle);
        Optional.ofNullable(request.description()).ifPresent(doctor::setDescription);
        doctor.setUpdatedAt(LocalDateTime.now());
        return doctorRepository.save(doctor);
    }

    @Override
    public List<DoctorEntity> findBySpeciality(String speciality) {
        return doctorRepository.findBySpeciality(speciality);
    }

    @Override
    public void deleteById(String id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<DoctorEntity> findByLastUpdatedAndLimit(LocalDateTime lastUpdated, Integer limit) {
        var queryLimit = Limit.of(limit+1);
        if (Objects.isNull(lastUpdated)){
            return doctorRepository.findAll(queryLimit);
        } else {
            return doctorRepository.findByLastUpdated(lastUpdated, queryLimit);
        }
    }
}
