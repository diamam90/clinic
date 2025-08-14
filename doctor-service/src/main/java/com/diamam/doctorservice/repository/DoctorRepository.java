package com.diamam.doctorservice.repository;

import com.diamam.doctorservice.entity.DoctorEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorRepository extends MongoRepository<DoctorEntity, String> {

    List<DoctorEntity> findBySpeciality(String speciality);

    @Query(value = "{}")
    List<DoctorEntity> findAll(Limit limit);

    @Query(value = "{ updatedAt: { $gt: ?0 }}")
    List<DoctorEntity> findByLastUpdated(LocalDateTime lastUpdated, Limit limit);
}
