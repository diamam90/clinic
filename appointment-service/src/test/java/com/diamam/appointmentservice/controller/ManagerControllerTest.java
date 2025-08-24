package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.dto.appointment.GenerateSingleDayRequest;
import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.mapper.AppointmentMapperImpl;
import com.diamam.appointmentservice.service.AutoGenerateAppointmentService;
import com.diamam.appointmentservice.service.GenerateAppointmentService;
import lombok.SneakyThrows;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
@Import(AppointmentMapperImpl.class)
class ManagerControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    GenerateAppointmentService generateAppointmentService;
    @MockBean
    AutoGenerateAppointmentService autoGenerateAppointmentService;

    @SneakyThrows
    @Test
    void shouldGenerateForFewDays() {

        var request = generateRequest();
        var appointments = generatedAppointmentMap();

        when(generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .thenReturn(appointments);

        mvc.perform(post("/api/v1/manager/appointments/generating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(generateForFewDaysRequest()))
                .andExpectAll(status().isCreated(),
                        content().json(generateForFewDaysOkResponse(), true));
    }

    @SneakyThrows
    @Test
    void generateForFewDaysEmptyRequest() {
        var request = generateRequest();
        var appointments = generatedAppointmentMap();

        when(generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .thenReturn(appointments);

        mvc.perform(post("/api/v1/manager/appointments/generating")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("status").value(400),
                        jsonPath("detail").value(
                                CombinableMatcher.both(StringContains.containsStringIgnoringCase("doctorId must not be blank"))
                                        .and(StringContains.containsStringIgnoringCase("days must not be null"))),
                        jsonPath("instance").value("/api/v1/manager/appointments/generating"));
    }

    @SneakyThrows
    @Test
    void generateForFewDaysNotPositiveDays() {

        var request = generateRequest();
        var appointments = generatedAppointmentMap();

        when(generateAppointmentService.generateByDoctorIdAndDaysCount(request))
                .thenReturn(appointments);

        mvc.perform(post("/api/v1/manager/appointments/generating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(generateForFewDaysNotPositiveDaysRequest()))
                .andExpectAll(status().isBadRequest(),
                        content().json(generateForFewDaysDaysMustBePositive(), true));
    }


    @SneakyThrows
    @Test
    void shouldGenerateSingleDay() {

        var request = generateSingleDayRequest();
        when(generateAppointmentService.generateByDoctorId(request)).thenReturn(generatedAppointments());

        mvc.perform(post("/api/v1/manager/appointments/generating/bydate")
                        .content("""
                                {
                                    "doctorId":"doctor256",
                                    "duration": "PT20M",
                                    "date":"2024-04-04",
                                    "shiftStart": "09:00:00",
                                    "shiftEnd": "21:00:00"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isCreated(),
                        content().json(generateSingleDayOkResponse(), true));
    }

    @SneakyThrows
    @Test
    void generateSingleDayEmptyRequest() {

        var request = generateSingleDayRequest();
        when(generateAppointmentService.generateByDoctorId(request)).thenReturn(generatedAppointments());

        mvc.perform(post("/api/v1/manager/appointments/generating/bydate")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("status").value(400),
                        jsonPath("detail").value(
                                CombinableMatcher.both(StringContains.containsStringIgnoringCase("doctorId must not be blank"))
                                        .and(StringContains.containsStringIgnoringCase("date must not be null"))),
                        jsonPath("instance").value("/api/v1/manager/appointments/generating/bydate")
                );
    }

    @SneakyThrows
    @Test
    void shouldAutoGenerate() {
        mvc.perform(post("/api/v1/manager/appointments/generating/auto"))
                .andExpect(status().isCreated());

        verify(autoGenerateAppointmentService).generateSchedule();
    }

    private GenerateRequest generateRequest() {
        return new GenerateRequest("doctor 228",
                Duration.ofMinutes(20),
                LocalDate.of(2025, 5, 5),
                LocalTime.of(8, 0, 0),
                LocalTime.of(13, 0, 0),
                12);
    }

    private GenerateSingleDayRequest generateSingleDayRequest() {
        return new GenerateSingleDayRequest("doctor256",
                Duration.parse("PT20M"),
                LocalDate.of(2024, 4, 4),
                LocalTime.of(9, 0, 0),
                LocalTime.of(21, 0, 0));

    }

    private Map<LocalDate, List<AppointmentEntity>> generatedAppointmentMap() {
        var appointment1 = new AppointmentEntity();
        appointment1.setId(12L);
        appointment1.setClientId("client 228");
        appointment1.setDoctorId("doctor id");
        appointment1.setStart(LocalTime.parse("08:00:00"));
        appointment1.setEnd(LocalTime.parse("09:00:00"));
        appointment1.setDate(LocalDate.parse("2025-05-05"));
        appointment1.setAvailable(true);

        var appointment2 = new AppointmentEntity();
        appointment2.setId(13L);
        appointment2.setClientId("client 229");
        appointment2.setDoctorId("doctor id");
        appointment2.setStart(LocalTime.parse("10:00:00"));
        appointment2.setEnd(LocalTime.parse("12:00:00"));
        appointment2.setDate(LocalDate.parse("2025-05-06"));
        appointment2.setAvailable(false);

        var appointment3 = new AppointmentEntity();
        appointment3.setId(14L);
        appointment3.setClientId("client 230");
        appointment3.setDoctorId("doctor id");
        appointment3.setStart(LocalTime.parse("12:00:00"));
        appointment3.setEnd(LocalTime.parse("14:00:00"));
        appointment3.setDate(LocalDate.parse("2025-05-06"));
        appointment3.setAvailable(true);

        return Map.of(
                LocalDate.parse("2025-05-05"), List.of(appointment1),
                LocalDate.parse("2025-05-06"), List.of(appointment2, appointment3)
        );
    }

    private List<AppointmentEntity> generatedAppointments() {
        var appointment1 = new AppointmentEntity();
        appointment1.setId(10L);
        appointment1.setDoctorId("doctor256");
        appointment1.setStart(LocalTime.parse("09:00:00"));
        appointment1.setEnd(LocalTime.parse("11:00:00"));
        appointment1.setDate(LocalDate.parse("2024-04-04"));
        appointment1.setAvailable(true);

        var appointment2 = new AppointmentEntity();
        appointment2.setId(11L);
        appointment2.setDoctorId("doctor256");
        appointment2.setStart(LocalTime.parse("11:00:00"));
        appointment2.setEnd(LocalTime.parse("14:00:00"));
        appointment2.setDate(LocalDate.parse("2024-04-04"));
        appointment2.setAvailable(false);

        var appointment3 = new AppointmentEntity();
        appointment3.setId(12L);
        appointment3.setDoctorId("doctor256");
        appointment3.setStart(LocalTime.parse("15:00:00"));
        appointment3.setEnd(LocalTime.parse("18:00:00"));
        appointment3.setDate(LocalDate.parse("2024-04-04"));
        appointment3.setAvailable(true);

        return List.of(appointment1, appointment2, appointment3);
    }

    private String generateForFewDaysRequest() {
        return """
                {
                    "doctorId": "doctor 228",
                    "duration": "PT20M",
                    "dateStart": "2025-05-05",
                    "shiftStart" : "08:00:00",
                    "shiftEnd": "13:00:00",
                    "days": 12
                }
                """;
    }

    private String generateForFewDaysNotPositiveDaysRequest() {
        return """
                {
                    "doctorId": "doctor 228",
                    "duration": "PT20M",
                    "dateStart": "2025-05-05",
                    "shiftStart" : "08:00:00",
                    "shiftEnd": "13:00:00",
                    "days": -12
                }
                """;
    }

    private String generateForFewDaysOkResponse() {
        return """
                {
                    "2025-05-05": [
                        {
                            "id" : 12,
                            "clientId": "client 228",
                            "doctorId": "doctor id",
                            "start": "08:00:00",
                            "end": "09:00:00",
                            "date": "2025-05-05",
                            "isAvailable": true
                        }
                    ],
                    "2025-05-06": [
                        {
                            "id" : 13,
                            "clientId": "client 229",
                            "doctorId": "doctor id",
                            "start": "10:00:00",
                            "end": "12:00:00",
                            "date": "2025-05-06",
                            "isAvailable": false
                        },
                        {
                            "id" : 14,
                            "clientId": "client 230",
                            "doctorId": "doctor id",
                            "start": "12:00:00",
                            "end": "14:00:00",
                            "date": "2025-05-06",
                            "isAvailable": true
                        }
                    ]
                }
                """;
    }

    private String generateForFewDaysDaysMustBePositive() {
        return """
                {
                     "type": "about:blank",
                     "title" : "Bad Request",
                     "status": 400,
                     "detail": "days must be positive",
                     "instance": "/api/v1/manager/appointments/generating"
                }
                """;
    }

    private String generateSingleDayOkResponse() {
        return """
                [
                    {
                            "id" : 10,
                            "doctorId": "doctor256",
                            "clientId": null,
                            "start": "09:00:00",
                            "end": "11:00:00",
                            "date": "2024-04-04",
                            "isAvailable": true
                    },
                    {
                            "id" : 11,
                            "doctorId": "doctor256",
                            "clientId": null,
                            "start": "11:00:00",
                            "end": "14:00:00",
                            "date": "2024-04-04",
                            "isAvailable": false
                    },
                     {
                            "id" : 12,
                            "doctorId": "doctor256",
                            "clientId": null,
                            "start": "15:00:00",
                            "end": "18:00:00",
                            "date": "2024-04-04",
                            "isAvailable": true
                    }
                ]
                """;
    }
}