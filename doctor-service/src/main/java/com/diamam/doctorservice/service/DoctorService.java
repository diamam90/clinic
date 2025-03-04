package com.diamam.doctorservice.service;

import com.diamam.doctorservice.entity.DoctorEntity;
import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.PatchDoctorRequest;
import com.diamam.doctorservice.model.UpdateDoctorRequest;

import java.util.List;

public interface DoctorService {
    DoctorEntity create(CreateDoctorRequest request);

    DoctorEntity update(String doctorId, UpdateDoctorRequest request);

    DoctorEntity patch(String doctorId, PatchDoctorRequest request);

    DoctorEntity findById(String id);

    List<DoctorEntity> findBySpeciality(String speciality);

    void deleteById(String id);

}
