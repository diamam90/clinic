package com.diamam.doctorservice.repository;

import com.diamam.doctorservice.entity.DoctorEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DoctorRepository extends ListCrudRepository<DoctorEntity, String> {
    List<DoctorEntity> findBySpeciality(String speciality);
}
