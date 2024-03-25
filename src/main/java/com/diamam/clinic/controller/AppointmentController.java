package com.diamam.clinic.controller;

import com.diamam.clinic.model.AppointmentInfo;
import com.diamam.clinic.model.DoctorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    List<DoctorInfo> doctorInfoList;
    List<AppointmentInfo> appointmentInfoList;

    {
        DoctorInfo doctor1 = new DoctorInfo(1L, "Ковырялко", "Красное-лечить", "хирург");
        DoctorInfo doctor2 = new DoctorInfo(2L, "Добрый", "Общеврач", "терапевт");
        DoctorInfo doctor3 = new DoctorInfo(3L, "Очкарёв", "Большие глаза", "окулист");
        doctorInfoList = List.of(doctor1, doctor2, doctor3);

        appointmentInfoList = IntStream.
                range(0, 100).
                limit(24).
                mapToObj(i ->
                        new AppointmentInfo(
                                i,
                                LocalDate.now(),
                                LocalTime.from(LocalTime.of(8, 0).plusMinutes(30 * i)),
                                LocalTime.from(LocalTime.of(8, 0).plusMinutes(30 * (i + 1))),
                                doctor1,
                                23L,
                                true
                        )).
                collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<AppointmentInfo>> getSpotsByDate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                appointmentInfoList
                        .stream()
                        .filter(appointment ->
                                appointment.date().equals(LocalDate.now()) && (id.equals(appointment.doctor().id())))
                        .toList(),
                HttpStatus.OK);
    }

}
