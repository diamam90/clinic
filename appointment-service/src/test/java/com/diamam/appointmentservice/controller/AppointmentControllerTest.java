package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.exception.BadRequestException;
import com.diamam.appointmentservice.mapper.AppointmentMapperImpl;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class)
@Import(AppointmentMapperImpl.class)
class AppointmentControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AppointmentService appointmentService;

    @SneakyThrows
    @Test
    void shouldReserve() {
        var appointment = appointmentEntityStub();

        when(appointmentService.reserve(228L, new ReserveRequest("client id")))
                .thenReturn(appointment);

        mvc.perform(post("/api/v1/appointments/228/reservation")
                        .content("""
                                    {
                                        "clientId": "client id"
                                    }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().json(reserveOkContent(), true));

    }

    @SneakyThrows
    @Test
    void reserveReturnsBadRequest() {
        when(appointmentService.reserve(228L, new ReserveRequest("client id")))
                .thenThrow(new BadRequestException("Appointment with id 228 not found"));

        mvc.perform(post("/api/v1/appointments/228/reservation")
                        .content("""
                                    {
                                        "clientId": "client id"
                                    }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest(),
                        content().json(reserveBadRequestContent(), true));
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"{}", " { \"clientId\": \"\"}"})
    void reserveWithInvalidClientIdReturnsBadRequest(String request) {
        mvc.perform(post("/api/v1/appointments/228/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpectAll(status().isBadRequest(),
                        content().json(reserveValidationFailedContent()));
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"appointment", "-114", "0"})
    void reserveWithInvalidAppointmentIdReturnsBadRequest(String appointmentId) {
        mvc.perform(post("/api/v1/appointments/{appointmentId}/reservation", appointmentId)
                        .content("""
                                    {
                                        "clientId": "client id"
                                    }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldCancel() {
        var appointment = appointmentEntityStub();
        appointment.setAvailable(true);
        appointment.setClientId(null);

        when(appointmentService.cancel(228L)).thenReturn(appointment);

        mvc.perform(post("/api/v1/appointments/228/cancelling"))
                .andExpectAll(status().isOk(),
                        content().json(cancelOkContent(), true));
    }

    @SneakyThrows
    @Test
    void cancelAppointmentNotFoundReturnsBadRequest() {
        var appointment = appointmentEntityStub();
        appointment.setAvailable(true);
        appointment.setClientId(null);

        when(appointmentService.cancel(228L))
                .thenThrow(new BadRequestException("Appointment with id 228 not found"));

        mvc.perform(post("/api/v1/appointments/228/cancelling"))
                .andExpectAll(status().isBadRequest(),
                        content().json(cancelBadRequestContent(), true));
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"appointment", "-114", "0"})
    void cancelWithInvalidAppointmentIdReturnsBadRequest(String appointmentId) {
        mvc.perform(post("/api/v1/appointments/{appointmentId}/cancelling", appointmentId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldFindById() {
        var appointment = appointmentEntityStub();

        when(appointmentService.findById(228L))
                .thenReturn(appointment);

        mvc.perform(get("/api/v1/appointments/228"))
                .andExpectAll(status().isOk(),
                        content().json(findByIdOkContent(), true));
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"appointment", "-114", "0"})
    void findByIdWithInvalidAppointmentIdReturnsBadRequest(String appointmentId) {
        mvc.perform(get("/api/v1/appointments/{appointmentId}", appointmentId))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldFind() {
        var appointment = appointmentEntityStub();
        var filter = new AppointmentFilter("doctor 228", "client 229", null);

        when(appointmentService.find(filter))
                .thenReturn(List.of(appointment));

        mvc.perform(get("/api/v1/appointments/filtration?doctorId={doctorId}&clientId={clientId}",
                        "doctor 228", "client 229"))
                .andExpectAll(status().isOk(),
                        content().json(findOkContent(), true));
    }

    @SneakyThrows
    @Test
    void findByFilterShouldReturnEmptyResult() {

        when(appointmentService.find(null))
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/appointments/filtration"))
                .andExpectAll(status().isOk(),
                        content().json(findNoContent(), true));
    }

    private AppointmentEntity appointmentEntityStub() {
        var appointment = new AppointmentEntity();
        appointment.setId(228L);
        appointment.setClientId("client id");
        appointment.setDoctorId("doctor id");
        appointment.setStart(LocalTime.of(8, 0, 0));
        appointment.setEnd(LocalTime.of(9, 0, 0));
        appointment.setDate(LocalDate.of(2025, 5, 5));
        appointment.setAvailable(false);

        return appointment;
    }

    private String reserveOkContent() {
        return """
                {
                    "id": 228,
                    "clientId": "client id",
                    "doctorId": "doctor id",
                    "start": "08:00:00",
                    "end": "09:00:00",
                    "date": "2025-05-05",
                    "isAvailable": false
                }
                """;
    }

    private String reserveBadRequestContent() {
        return """
                    {
                        "type": "about:blank",
                        "title" : "Bad Request",
                        "status": 400,
                        "detail": "Appointment with id 228 not found",
                        "instance": "/api/v1/appointments/228/reservation"
                    }
                """;
    }

    private String reserveValidationFailedContent() {
        return """
                {
                     "type": "about:blank",
                     "title" : "Bad Request",
                     "status": 400,
                     "detail": "clientId must not be blank",
                     "instance": "/api/v1/appointments/228/reservation"
                }
                """;
    }

    private String cancelOkContent() {
        return """
                {
                    "id": 228,
                    "clientId": null,
                    "doctorId": "doctor id",
                    "start": "08:00:00",
                    "end": "09:00:00",
                    "date": "2025-05-05",
                    "isAvailable": true
                }
                """;
    }

    private String cancelBadRequestContent() {
        return """
                    {
                        "type": "about:blank",
                        "title" : "Bad Request",
                        "status": 400,
                        "detail": "Appointment with id 228 not found",
                        "instance": "/api/v1/appointments/228/cancelling"
                    }
                """;
    }

    private String findByIdOkContent() {
        return """
                {
                    "id": 228,
                    "clientId": "client id",
                    "doctorId": "doctor id",
                    "start": "08:00:00",
                    "end": "09:00:00",
                    "date": "2025-05-05",
                    "isAvailable": false
                }
                """;
    }

    private String findOkContent() {
        return """
                [{
                    "id": 228,
                    "clientId": "client id",
                    "doctorId": "doctor id",
                    "start": "08:00:00",
                    "end": "09:00:00",
                    "date": "2025-05-05",
                    "isAvailable": false
                }]
                """;
    }

    private String findNoContent() {
        return "[]";
    }
}