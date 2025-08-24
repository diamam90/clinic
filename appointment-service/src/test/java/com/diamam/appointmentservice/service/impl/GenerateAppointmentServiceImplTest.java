package com.diamam.appointmentservice.service.impl;

import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.dto.appointment.GenerateSingleDayRequest;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.exception.BadRequestException;
import com.diamam.appointmentservice.repository.AppointmentRepository;
import com.diamam.appointmentservice.service.GenerateAppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = GenerateAppointmentServiceImpl.class)
class GenerateAppointmentServiceImplTest {

    @MockBean
    AppointmentRepository appointmentRepository;

    @Autowired
    GenerateAppointmentService generateAppointmentService;

    @Captor
    ArgumentCaptor<List<AppointmentEntity>> appointmentCaptor;

    private static final String shiftStartStr = "08:00:00";
    private static final String shiftEndStr = "20:00:00";

    @Test
    void shouldGenerateByDoctorAndDaysCount() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(14, 40, 0),
                5
        );

        var appointment1 = appointment_1();
        var appointment2 = appointment_2();
        var appointment3 = appointment_3();
        var appointment4 = appointment_4();

        when(appointmentRepository.saveAll(any())).thenReturn(List.of(appointment4, appointment1,
                appointment3, appointment2));

        var appointmentMap = generateAppointmentService.generateByDoctorIdAndDaysCount(request);

        verify(appointmentRepository).saveAll(appointmentCaptor.capture());

        var first = appointmentCaptor.getValue().getFirst();
        var last = appointmentCaptor.getValue().getLast();

        assertThat(first)
                .hasFieldOrPropertyWithValue("doctorId", "doctor256")
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2025, 1, 1))
                .hasFieldOrPropertyWithValue("start", LocalTime.of(10, 0, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(10, 30, 0))
                .hasFieldOrPropertyWithValue("isAvailable", true)
                .hasFieldOrPropertyWithValue("clientId", null);

        assertThat(last)
                .hasFieldOrPropertyWithValue("doctorId", "doctor256")
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2025, 1, 5))
                .hasFieldOrPropertyWithValue("start", LocalTime.of(14, 0, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(14, 30, 0))
                .hasFieldOrPropertyWithValue("isAvailable", true)
                .hasFieldOrPropertyWithValue("clientId", null);

        assertThat(appointmentMap)
                .hasSize(3)
                .containsEntry(LocalDate.of(2025, 1, 1),
                        List.of(appointment1, appointment2))
                .containsEntry(LocalDate.of(2025, 1, 2),
                        List.of(appointment3))
                .containsEntry(LocalDate.of(2025, 1, 3),
                        List.of(appointment4));
    }

    @Test
    void generateByDoctorAndDaysCount_WhenShiftStartIsBeforeWorkingDayStart_ShouldThrowBadRequest() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.parse(shiftStartStr).minusNanos(1),
                LocalTime.of(14, 40, 0),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorAndDaysCount_WhenShiftStartIsAfterWorkingDayEnd_ShouldThrowBadRequest() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.parse(shiftEndStr).plusNanos(1),
                LocalTime.of(14, 40, 0),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorAndDaysCount_WhenShiftEndIsAfterWorkingDayEnd_ShouldThrowBadRequest() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(8, 0, 0),
                LocalTime.parse(shiftEndStr).plusNanos(1),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }


    @Test
    void generateByDoctorAndDaysCount_WhenShiftEndIsABeforeWorkingDayStart_ShouldThrowBadRequest() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(8, 0, 0),
                LocalTime.parse(shiftStartStr).minusNanos(1),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorAndDaysCount_WhenShiftEndIsBeforeShiftStart_ShouldThrowBadRequest() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(9, 0, 3),
                LocalTime.of(9, 0, 2),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorAndDaysCount_WhenAppointmentsByDateAlreadyExist_ShouldThrowBadRequest() {

        var startDate = LocalDate.of(2025, 1, 1);
        var endDate = LocalDate.of(2025, 1, 6);

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                startDate,
                LocalTime.of(8, 40, 0),
                LocalTime.of(14, 40, 0),
                5
        );

        when(appointmentRepository.existByDoctorAndDateBetween("doctor256", startDate, endDate))
                .thenReturn(true);

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorIdAndDaysCount_WhenShiftRequestIsLessThanDuration_ShouldReturnEmptyMap() {

        var request = new GenerateRequest(
                "doctor256",
                Duration.ofMinutes(30),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(8, 40, 0),
                LocalTime.of(9, 0, 0),
                5
        );

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .isExactlyInstanceOf(BadRequestException.class);
        verify(appointmentRepository, never()).saveAll(any());
    }


    @Test
    void shouldGenerateByDoctorId() {

        var request = new GenerateSingleDayRequest("doctor256",
                Duration.ofMinutes(10L),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(9, 0, 0),
                LocalTime.of(10, 0, 0));

        generateAppointmentService.generateByDoctorId(request);

        verify(appointmentRepository).saveAll(appointmentCaptor.capture());

        var actualAppointments = appointmentCaptor.getValue();

        actualAppointments.forEach(
                app -> assertThat(app)
                        .hasFieldOrPropertyWithValue("doctorId", "doctor256")
                        .hasFieldOrPropertyWithValue("date", LocalDate.of(2025, 1, 1))
                        .hasFieldOrPropertyWithValue("isAvailable", true)
                        .hasFieldOrPropertyWithValue("clientId", null)
                        .extracting("start", "end")
                        .isNotEmpty()
        );

        assertThat(actualAppointments.getFirst())
                .hasFieldOrPropertyWithValue("start", LocalTime.of(9, 0, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(9, 10, 0));

        assertThat(actualAppointments.getLast())
                .hasFieldOrPropertyWithValue("start", LocalTime.of(9, 50, 0))
                .hasFieldOrPropertyWithValue("end", LocalTime.of(10, 0, 0));
    }

    @Test
    void generateByDoctorId_WhenStartShiftIsBeforeWorkingDayStart_ShouldThrowBadRequest() {
        var request = new GenerateSingleDayRequest("doctor256",
                Duration.ofMinutes(10L),
                LocalDate.of(2025, 1, 1),
                LocalTime.parse(shiftStartStr).minusNanos(1),
                LocalTime.of(10, 0, 0));

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorId(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void generateByDoctorId_WhenEndShiftIsAfterWorkingDayStart_ShouldThrowBadRequest() {
        var request = new GenerateSingleDayRequest("doctor256",
                Duration.ofMinutes(10L),
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.parse(shiftEndStr).plusNanos(1));

        assertThatThrownBy(() -> generateAppointmentService.generateByDoctorId(request))
                .isExactlyInstanceOf(BadRequestException.class);
    }


    private AppointmentEntity appointment_1() {
        var appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setDoctorId("doctor256");
        appointment.setDate(LocalDate.of(2025, 1, 1));
        appointment.setAvailable(true);
        appointment.setStart(LocalTime.of(10, 0, 0));
        appointment.setEnd(LocalTime.of(10, 0, 0));

        return appointment;
    }

    private AppointmentEntity appointment_2() {
        var appointment = new AppointmentEntity();
        appointment.setId(2L);
        appointment.setDoctorId("doctor256");
        appointment.setDate(LocalDate.of(2025, 1, 1));
        appointment.setAvailable(true);
        appointment.setStart(LocalTime.of(10, 30, 0));
        appointment.setEnd(LocalTime.of(11, 0, 0));

        return appointment;
    }

    private AppointmentEntity appointment_3() {
        var appointment = new AppointmentEntity();
        appointment.setId(3L);
        appointment.setDoctorId("doctor256");
        appointment.setDate(LocalDate.of(2025, 1, 2));
        appointment.setAvailable(true);
        appointment.setStart(LocalTime.of(11, 30, 0));
        appointment.setEnd(LocalTime.of(12, 0, 0));

        return appointment;
    }

    private AppointmentEntity appointment_4() {
        var appointment = new AppointmentEntity();
        appointment.setId(3L);
        appointment.setDoctorId("doctor256");
        appointment.setDate(LocalDate.of(2025, 1, 3));
        appointment.setAvailable(true);
        appointment.setStart(LocalTime.of(13, 0, 0));
        appointment.setEnd(LocalTime.of(14, 30, 0));

        return appointment;
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("appointment-service.working-day-start", () -> shiftStartStr);
        registry.add("appointment-service.working-day-end", () -> shiftEndStr);
    }


}