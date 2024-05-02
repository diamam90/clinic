package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Appointment;
import com.diamam.clinic.entity.Client;
import com.diamam.clinic.entity.Doctor;
import com.diamam.clinic.exception.NotFoundException;
import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.UpdateAppointmentDto;
import com.diamam.clinic.model.doctor.DoctorInfo;
import com.diamam.clinic.repository.AppointmentRepository;
import com.diamam.clinic.repository.ClientRepository;
import com.diamam.clinic.repository.DoctorRepository;
import com.diamam.clinic.stub.AppointmentStubs;
import com.diamam.clinic.stub.ClientStubs;
import com.diamam.clinic.stub.DoctorStubs;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    AppointmentServiceImpl appointmentService;
    @Captor
    ArgumentCaptor<List<Appointment>> appointmentListCaptor;

    @Test
    void make() {
        Appointment appointment = AppointmentStubs.savedFreeSpot_1();
        Client alex = ClientStubs.savedAlex();
        appointment.setClient(alex);

        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
        when(clientRepository.findById(alex.getId())).thenReturn(Optional.of(alex));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        UpdateAppointmentDto dto =
                new UpdateAppointmentDto(appointment.getId(), alex.getId(), appointment.getDoctor().getId());
        appointmentService.make(dto);

        verify(appointmentRepository).save(eq(appointment));
    }

    @Test
    void makeThrowsAppointmentNotFoundException() {
        UpdateAppointmentDto dto = new UpdateAppointmentDto(
                "23", "client id", "doctor id");
        assertThatThrownBy(() -> appointmentService.make(dto))
                .isInstanceOf(NotFoundException.AppointmentNotFoundException.class);
    }

    @Test
    void makeThrowsClientNotFoundException() {
        Appointment appointment = AppointmentStubs.savedFreeSpot_1();
        UpdateAppointmentDto dto = new UpdateAppointmentDto(
                appointment.getId(),
                "1234",
                appointment.getDoctor().getId()
        );

        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
        assertThatThrownBy(() -> appointmentService.make(dto))
                .isInstanceOf(NotFoundException.ClientNotFoundException.class);
    }

    @Test
    void delete() {
        String id = "25";
        appointmentService.deleteById(id);
        verify(appointmentRepository).deleteById(id);
    }

    @Test
    void deleteThrowAppointmentNotFoundException() {
        String id = "25";
        doThrow(IllegalArgumentException.class).when(appointmentRepository).deleteById(id);
        assertThatThrownBy(() -> appointmentService.deleteById(id))
                .isInstanceOf(NotFoundException.AppointmentNotFoundException.class);
    }

    @Test
    void getById() {
        Appointment appointment = AppointmentStubs.savedFreeSpot_1();
        when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
        AppointmentInfo appointmentInfo = appointmentService.getById(appointment.getId());
        assertEquals(AppointmentInfo.toDto(appointment), appointmentInfo);
    }

    @Test
    void getByIdThrowsAppointmentNotFoundException() {
        assertThatThrownBy(() -> appointmentService.getById("94"))
                .isInstanceOf(NotFoundException.AppointmentNotFoundException.class);
    }

    @Test
    void getByClientId() {
        Appointment appointment = AppointmentStubs.appointment_1();
        when(appointmentRepository.findByClientId(appointment.getClient().getId())).thenReturn(List.of(appointment));
        List<AppointmentInfo> actual = appointmentService.getByClientId(appointment.getClient().getId());
        assertThat(actual).isEqualTo(List.of(AppointmentInfo.toDto(appointment)));
    }

    @Test
    void getFreeSpotByDoctorIdAndDate() {
        LocalDate date = LocalDate.of(2000, 10, 10);
        Appointment appointment = AppointmentStubs.savedFreeSpot_1();
        when(appointmentRepository.findByDateAndDoctorIdAndClientIsNull(date,
                appointment.getDoctor().getId())).thenReturn(List.of(appointment));
        List<AppointmentInfo> actual = appointmentService.
                getFreeSpotByDoctorIdAndDate(appointment.getDoctor().getId(), date);
        assertThat(actual).isEqualTo(List.of(AppointmentInfo.toDto(appointment)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void generateSchedule() {
        Doctor doctor = DoctorStubs.savedIvanIvanov();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
         appointmentService.generateSchedule(doctor.getId(), LocalDate.of(111, 11, 11), 7);
        verify(appointmentRepository).saveAll(appointmentListCaptor.capture());
        assertThat(appointmentListCaptor.getValue()).hasSize(126);
    }

    @Test
    void generateScheduleThrowsDoctorNotFoundException() {
        assertThatThrownBy(
                () -> appointmentService.generateSchedule("23", LocalDate.of(111, 11, 11), 7))
                .isInstanceOf(NotFoundException.DoctorNotFoundException.class);
    }

    @Test
    void generateScheduleOneDay() {
        Doctor doctor = DoctorStubs.savedIvanIvanov();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        appointmentService.generateScheduleOneDay(doctor.getId(), LocalDate.of(111, 11, 11));
        verify(appointmentRepository).saveAll(appointmentListCaptor.capture());
        assertThat(appointmentListCaptor.getValue()).hasSize(18);
    }

    @Test
    void generateScheduleOneDayThrowsDoctorNotFoundException() {
        assertThatThrownBy(
                () -> appointmentService.generateScheduleOneDay("23", LocalDate.of(111, 11, 11)))
                .isInstanceOf(NotFoundException.DoctorNotFoundException.class);
    }

    @DisplayName("Should generate 7 (day) * 18 appointments")
    @Test
    void generateAppointments() {
        Doctor doctor = DoctorStubs.savedIvanIvanov();
        LocalDate date = LocalDate.of(1000, 10, 21);

        List<Appointment> appointments = appointmentService.generateAppointments(doctor, date, 7);
        assertThat(appointments)
                .hasSize(126)
                .usingRecursiveComparison(RecursiveComparisonConfiguration
                        .builder()
                        .withComparedFields("doctor")
                        .build()).isEqualTo(DoctorInfo.toDto(doctor));
    }
}