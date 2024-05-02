package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.stub.DoctorStubs;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorRepositoryTest extends AbstractRepositoryTest {


    @BeforeEach
    void clear() {
        mongoOperations.dropCollection("doctor");
    }

    @Test
    void shouldSaveDoctor() {
        Doctor withoutId = DoctorStubs.ivanIvanov();

        Doctor saved = doctorRepository.save(withoutId);

        List<Doctor> doctors = mongoOperations.find(Query.query(Criteria.where("id").is(saved.getId())), Doctor.class);
        assertThat(doctors).hasSize(1);
        assertThat(doctors.get(0)).hasFieldOrProperty("id").
                usingRecursiveComparison(
                        RecursiveComparisonConfiguration.builder().withIgnoredFields("id").build()).
                isEqualTo(withoutId);
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor peterDmitriev = DoctorStubs.peterDmitriev();
        Doctor saved = mongoOperations.save(peterDmitriev, "doctor");
        Doctor forUpdate = DoctorStubs.ivanIvanov();
        forUpdate.setId(saved.getId());

        doctorRepository.save(forUpdate);

        List<Doctor> doctors = mongoOperations.find(Query.query(Criteria.where("id").is(forUpdate.getId())), Doctor.class, "doctor");
        assertThat(doctors).hasSize(1);
        assertThat(doctors.get(0)).usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFields("id").build()).isEqualTo(forUpdate);
    }

    @Test
    void shouldDeleteDoctor() {
        Doctor saved = mongoOperations.save(DoctorStubs.ivanIvanov());
        List<Doctor> beforeDelete = mongoOperations.find(Query.query(new Criteria()), Doctor.class);
        doctorRepository.delete(saved);

        List<Doctor> afterDelete = mongoOperations.find(Query.query(new Criteria()), Doctor.class);
        assertThat(beforeDelete).hasSize(1);
        assertThat(afterDelete).isEmpty();
    }

    @Test
    void shouldReturnDoctorById() {
        Doctor peterDmitriev = DoctorStubs.peterDmitriev();

        Doctor saved = mongoOperations.save(peterDmitriev, "doctor");
        Optional<Doctor> optional = doctorRepository.findById(saved.getId());
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(saved);
    }

    @Test
    void shouldFindTwoDoctors() {
        Doctor ivanIvanov = DoctorStubs.ivanIvanov();
        Doctor peterDmitriev = DoctorStubs.peterDmitriev();
        mongoOperations.save(ivanIvanov);
        mongoOperations.save(peterDmitriev);

        List<Doctor> all = doctorRepository.findAll();
        assertThat(all).hasSize(2);
    }
}


