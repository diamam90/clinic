package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.client.DoctorClient;
import com.diamam.appointmentservice.model.doctorservice.DoctorIdsResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoGenerateAppointmentServiceTest {


    @InjectMocks
    AutoGenerateAppointmentService autoGenerateService;

    @Mock
    GenerateAppointmentService generateAppointmentService;
    @Mock
    DoctorClient client;
    @Mock
    Clock clock;

    static Integer TEST_DAYS_COUNT = 6;

    @SneakyThrows
    @Test
    void shouldGenerateSchedule() {
        Field field = autoGenerateService.getClass().getDeclaredField("batchSize");
        field.setAccessible(true);
        field.set(autoGenerateService, 30);

        field = autoGenerateService.getClass().getDeclaredField("daysCount");
        field.setAccessible(true);
        field.set(autoGenerateService, TEST_DAYS_COUNT);

        when(client.getDoctorIds(null, 30)).thenReturn(doctorIdsResponse_1());
        when(client.getDoctorIds(LocalDateTime.parse("2025-12-12T10:00:00"), 30)).thenReturn(doctorIdsResponse_2());

        when(clock.instant()).thenReturn(Instant.parse("2025-06-06T12:00:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        autoGenerateService.generateSchedule();

        verify(client, times(2)).getDoctorIds(any(), any());
        verify(generateAppointmentService, times(3)).generateByDoctorIdAndDaysCount(any());
    }

    @Test
    void generateSchedule_WhenDoctorClientReturnsNull_ShouldDoNothing() {
        when(client.getDoctorIds(any(), any())).thenReturn(null);

        autoGenerateService.generateSchedule();

        verify(client).getDoctorIds(any(), any());
        verify(generateAppointmentService, never()).generateByDoctorIdAndDaysCount(any());
    }

    @Test
    void generateSchedule_WhenDoctorClientReturnsEmptyList_ShouldDoNothing() {

        var doctorIdsResponse = new DoctorIdsResponse(Collections.emptyList(),
                LocalDateTime.of(2025, 4, 4, 4, 4, 4),
                true);

        when(client.getDoctorIds(any(), any())).thenReturn(doctorIdsResponse);

        autoGenerateService.generateSchedule();

        verify(client).getDoctorIds(any(), any());
        verify(generateAppointmentService, never()).generateByDoctorIdAndDaysCount(any());
    }

    @Test
    void generateSchedule_WhenDoctorClientReturnsHasNextIsFalse_ShouldExecuteExactlyOnce() {

        when(client.getDoctorIds(any(), any())).thenReturn(doctorIdsResponse_2());

        when(clock.instant()).thenReturn(Instant.parse("2025-06-06T12:00:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        autoGenerateService.generateSchedule();

        verify(client).getDoctorIds(any(), any());
        verify(generateAppointmentService).generateByDoctorIdAndDaysCount(any());
    }


    private DoctorIdsResponse doctorIdsResponse_1() {
        return new DoctorIdsResponse(List.of("doctor1", "doctor2"),
                LocalDateTime.of(2025, 12, 12, 10, 0, 0),
                true);
    }

    private DoctorIdsResponse doctorIdsResponse_2() {
        return new DoctorIdsResponse(List.of("doctor3"),
                LocalDateTime.of(2025, 12, 12, 12, 16, 0),
                false);
    }
}