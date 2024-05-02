package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
}
