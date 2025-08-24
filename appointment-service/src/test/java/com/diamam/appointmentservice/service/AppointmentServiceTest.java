package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.configuration.AppConfig;
import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.exception.BadRequestException;
import com.diamam.appointmentservice.repository.AppointmentRepository;
import com.diamam.appointmentservice.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AppConfig.class, AppointmentServiceImpl.class})
class AppointmentServiceTest {

    @MockBean
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentServiceImpl appointmentService;

    @MockBean
    Clock clock;

    static final Integer NUMBER_OF_DAYS = 5;

    @Test
    void shouldReserve() {

        var appointment = appointmentStub_1();
        var reserveRequest = new ReserveRequest("acbcbs12123");
        when(appointmentRepository.findById(12L)).thenReturn(Optional.of(appointment));
        var reserved = appointmentService.reserve(12L, reserveRequest);

        assertThat(reserved)
                .hasFieldOrPropertyWithValue("clientId", "acbcbs12123")
                .hasFieldOrPropertyWithValue("isAvailable", false)
                .usingRecursiveComparison()
                .ignoringFields("clientId", "isAvailable")
                .isEqualTo(appointment);
    }

    @Test
    void reserveThrowsBadRequest() {
        var reserveRequest = new ReserveRequest("acbcbs12123");
        when(appointmentRepository.findById(12L)).thenThrow(new BadRequestException("Appointment with id not found"));
        assertThrows(BadRequestException.class, () -> appointmentService.reserve(12L, reserveRequest));
    }

    @Test
    void shouldCancel() {

        var appointment = appointmentStub_2();
        when(appointmentRepository.findById(12L)).thenReturn(Optional.of(appointment));
        var cancelled = appointmentService.cancel(12L);

        assertThat(cancelled)
                .hasFieldOrPropertyWithValue("clientId", null)
                .hasFieldOrPropertyWithValue("isAvailable", true)
                .usingRecursiveComparison()
                .ignoringFields("clientId", "isAvailable")
                .isEqualTo(appointment);
    }

    @Test
    void cancelThrowsBadRequest() {
        when(appointmentRepository.findById(12L)).thenThrow(new BadRequestException("Appointment with id not found"));
        assertThrows(BadRequestException.class, () -> appointmentService.cancel(12L));
    }

    @Test
    void shouldFindById() {
        var appointment = appointmentStub_1();
        when(appointmentRepository.findById(12L)).thenReturn(Optional.of(appointment));
        var found = appointmentService.findById(12L);

        assertThat(found)
                .usingRecursiveComparison()
                .isEqualTo(appointment);
    }

    @Test
    void findByIdThrowsBadRequest() {
        when(appointmentRepository.findById(12L)).thenThrow(new BadRequestException("Appointment with id not found"));
        assertThrows(BadRequestException.class, () -> appointmentService.findById(12L));
    }

    @Test
    void findByFilterWhenClientIdIsPresentWithoutDate() {
        var filter = new AppointmentFilter("doctor234", "client34", null);
        appointmentService.find(filter);
        verify(appointmentRepository).findByClientId("client34");
        verify(appointmentRepository, never()).findByClientIdAndDate(any(), any());
        verify(appointmentRepository, never()).findByDoctorId(any());
        verify(appointmentRepository, never()).findByDoctorIdAndDate(any(), any());
    }

    @Test
    void findByFilterWhenClientIdIsPresentWithDate() {
        var filter = new AppointmentFilter("doctor234", "client34", LocalDate.of(2020, 10, 10));
        appointmentService.find(filter);
        verify(appointmentRepository).findByClientIdAndDate("client34", LocalDate.parse("2020-10-10"));
        verify(appointmentRepository, never()).findByClientId(any());
        verify(appointmentRepository, never()).findByDoctorId(any());
        verify(appointmentRepository, never()).findByDoctorIdAndDate(any(), any());
    }

    @Test
    void findByFilterWhenDoctorIdIsPresentWithoutDate() {
        var filter = new AppointmentFilter("doctor234", null, null);
        appointmentService.find(filter);
        verify(appointmentRepository).findByDoctorId("doctor234");
        verify(appointmentRepository, never()).findByClientIdAndDate(any(), any());
        verify(appointmentRepository, never()).findByClientId(any());
        verify(appointmentRepository, never()).findByDoctorIdAndDate(any(), any());
    }

    @Test
    void findByFilterWhenDoctorIdIsPresentWithDate() {
        var filter = new AppointmentFilter("doctor234", null, LocalDate.of(2023, 3, 3));
        appointmentService.find(filter);
        verify(appointmentRepository).findByDoctorIdAndDate("doctor234", LocalDate.parse("2023-03-03"));
        verify(appointmentRepository, never()).findByDoctorId(any());
        verify(appointmentRepository, never()).findByClientIdAndDate(any(), any());
        verify(appointmentRepository, never()).findByClientId(any());
    }

    @ParameterizedTest
    @MethodSource(value = "filters")
    void findByEmptyFilter(AppointmentFilter filter) {
        var result = appointmentService.find(filter);

        assertThat(result).isEmpty();

        verify(appointmentRepository, never()).findByDoctorIdAndDate(any(), any());
        verify(appointmentRepository, never()).findByDoctorId(any());
        verify(appointmentRepository, never()).findByClientIdAndDate(any(), any());
        verify(appointmentRepository, never()).findByClientId(any());
    }

    @Test
    void shouldFindByDoctorAndDateBetween() {

        var doctorId = "doctor2312";
        var start = LocalDate.of(2030, 10, 10);
        var end = LocalDate.of(2030, 10, 12);

        var actual = List.of(appointmentStub_1(), appointmentStub_2());
        when(appointmentRepository.findByDoctorIdAndDateBetween(doctorId, start, end)).thenReturn(actual);

        var expected = appointmentService.findByDoctorIdAndDateBetween(doctorId, start, end);
        assertEquals(expected, actual);
    }

    @Test
    void findByDoctorIdAndDateBetweenWithoutDates() {
        var doctorId = "doctor2312";
        var start = LocalDate.of(2030, 10, 10);
        var end = start.plusDays(NUMBER_OF_DAYS);

        Clock fixed = Clock.fixed(
                LocalDateTime.of(2030, 10, 10, 10, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );

        var expected = List.of(appointmentStub_1(), appointmentStub_2());
        when(appointmentRepository.findByDoctorIdAndDateBetween(doctorId, start, end))
                .thenReturn(expected);
        when(clock.instant()).thenReturn(fixed.instant());
        when(clock.getZone()).thenReturn(fixed.getZone());

        var actual = appointmentService.findByDoctorIdAndDateBetween(doctorId, null, null);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindByClientId() {
        var expectedAppointments = List.of(appointmentStub_2());
        var clientId = "client256";

        when(appointmentRepository.findByClientId(clientId)).thenReturn(expectedAppointments);

        var actualAppointments = appointmentService.findByClientId(clientId);
        assertEquals(expectedAppointments, actualAppointments);
    }

    private static Stream<AppointmentFilter> filters() {
        return Stream.of(null, new AppointmentFilter(null, null, null));
    }

    private AppointmentEntity appointmentStub_1() {
        var appointment = new AppointmentEntity();
        appointment.setDoctorId("doctor123123");
        appointment.setAvailable(true);
        appointment.setDate(LocalDate.of(2025, 1, 1));
        appointment.setStart(LocalTime.of(10, 1, 1));
        appointment.setEnd(LocalTime.of(11, 0, 0));

        return appointment;
    }

    private AppointmentEntity appointmentStub_2() {
        var appointment = new AppointmentEntity();
        appointment.setDoctorId("doctor123123");
        appointment.setClientId("client123123");
        appointment.setAvailable(true);
        appointment.setDate(LocalDate.of(2025, 1, 1));
        appointment.setStart(LocalTime.of(10, 1, 1));
        appointment.setEnd(LocalTime.of(11, 0, 0));

        return appointment;
    }

    @DynamicPropertySource
    public static void registry(DynamicPropertyRegistry registry) {
        registry.add("appointment-service.schedule.number-of-days", () -> NUMBER_OF_DAYS);
    }
}