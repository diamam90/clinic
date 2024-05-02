package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Appointment;
import com.diamam.clinic.entity.Client;
import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.stub.AppointmentStubs;
import com.diamam.clinic.stub.ClientStubs;
import com.diamam.clinic.stub.DoctorStubs;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentRepositoryTest extends AbstractRepositoryTest {

    @BeforeEach
    void clear() {
        mongoOperations.dropCollection("appointment");
        mongoOperations.dropCollection("clinic");
        mongoOperations.dropCollection("doctor");
    }

    @Test
    void shouldCreateAppointmentWithDoctor() {
        Appointment appointment = AppointmentStubs.freeSpot_1();
        Doctor doctor = DoctorStubs.ivanIvanov();
        Doctor savedDoctor = doctorRepository.save(doctor);
        appointment.setDoctor(savedDoctor);

        Appointment saved = appointmentRepository.save(appointment);

        List<Appointment> appointmentList = mongoOperations.find(
                Query.query(Criteria.where("id").is(saved.getId())), Appointment.class);
        assertThat(appointmentList).hasSize(1);
        assertThat(appointmentList.get(0)).hasFieldOrProperty("doctor");
        assertThat(appointmentList.get(0)).usingRecursiveComparison().isEqualTo(saved);
    }

    @Test
    void shouldFindAppointmentByDateAndDoctorIdAndClientIsNull() {
        Appointment appointment = AppointmentStubs.freeSpot_1();
        Doctor doctor = DoctorStubs.ivanIvanov();
        Doctor savedDoctor = doctorRepository.save(doctor);
        appointment.setDoctor(savedDoctor);

        Appointment saved = mongoOperations.save(appointment);

        List<Appointment> actual = appointmentRepository.findByDateAndDoctorIdAndClientIsNull(appointment.getDate(), doctor.getId());
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0))
                .hasFieldOrProperty("doctor")
                .hasFieldOrPropertyWithValue("client", null);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(saved);
    }

    @Test
    void shouldNotFindAppointmentByDateAndDoctorIdAndClientIsNull() {
        Appointment appointment = AppointmentStubs.freeSpot_1();
        Doctor doctor = DoctorStubs.ivanIvanov();
        Doctor savedDoctor = doctorRepository.save(doctor);
        appointment.setDoctor(savedDoctor);
        Client client = ClientStubs.alex();
        Client savedClient = clientRepository.save(client);
        appointment.setClient(savedClient);

        List<Appointment> actual = appointmentRepository.findByDateAndDoctorIdAndClientIsNull(appointment.getDate(), doctor.getId());
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindByDoctorIdAndClientIdAndDate() {
        Appointment appointment = AppointmentStubs.freeSpot_1();
        Doctor doctor = DoctorStubs.ivanIvanov();
        Doctor savedDoctor = doctorRepository.save(doctor);
        appointment.setDoctor(savedDoctor);
        Client client = ClientStubs.alex();
        Client savedClient = clientRepository.save(client);
        appointment.setClient(savedClient);
        appointmentRepository.save(appointment);

        Optional<Appointment> actual = appointmentRepository.
                findByDoctorIdAndClientIdAndDate(savedDoctor.getId(), savedClient.getId(), appointment.getDate());
        assertThat(actual).isPresent()
                .has(new Condition<>(o -> Objects.nonNull(o.get().getClient()), "client must not be null", actual))
                .has(new Condition<>(o -> Objects.nonNull(o.get().getDoctor()), "doctor must not be null", actual));
    }
}