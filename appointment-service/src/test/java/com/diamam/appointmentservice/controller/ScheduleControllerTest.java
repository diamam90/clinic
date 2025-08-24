package com.diamam.appointmentservice.controller;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.mapper.AppointmentMapperImpl;
import com.diamam.appointmentservice.mapper.AppointmentScheduleMapper;
import com.diamam.appointmentservice.service.AppointmentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@Import({AppointmentScheduleMapper.class, AppointmentMapperImpl.class})
class ScheduleControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    AppointmentService appointmentService;

    @SneakyThrows
    @Test
    void shouldFind() {
        when(appointmentService.findByDoctorIdAndDateBetween("doctor256",
                LocalDate.of(2025, 6, 6),
                null))
                .thenReturn(appointments());

        mvc.perform(get("/api/v1/schedules/doctor256?start={start}", "2025-06-06"))
                .andExpectAll(status().isOk(),
                        content().json(generateForFewDaysOkResponse()));
    }

    private List<AppointmentEntity> appointments() {
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
        appointment2.setClientId("client666");
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

        var appointment4 = new AppointmentEntity();
        appointment4.setId(13L);
        appointment4.setDoctorId("doctor256");
        appointment4.setStart(LocalTime.parse("10:00:00"));
        appointment4.setEnd(LocalTime.parse("12:00:00"));
        appointment4.setDate(LocalDate.parse("2024-04-05"));
        appointment4.setAvailable(true);

        return List.of(appointment1, appointment2, appointment3, appointment4);
    }


    private String generateForFewDaysOkResponse() {
        return """
                [{
                    "date": "2024-04-04",
                     "appointments": [
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
                            "clientId": "client666",
                            "doctorId": "doctor256",
                            "start": "11:00:00",
                            "end": "14:00:00",
                            "date": "2024-04-04",
                            "isAvailable": false
                        }, {
                            "id" : 12,
                            "doctorId": "doctor256",
                            "clientId": null,
                            "start": "15:00:00",
                            "end": "18:00:00",
                            "date": "2024-04-04",
                            "isAvailable": true
                        }
                    ],
                    "isAvailable": true
                },
                {
                    "date": "2024-04-05",
                     "appointments": [
                        {
                            "id" : 13,
                            "doctorId": "doctor256",
                            "clientId": null,
                            "start": "10:00:00",
                            "end": "12:00:00",
                            "date": "2024-04-05",
                            "isAvailable": true
                        }
                    ],
                    "isAvailable": true
                }]
                """;
    }

}