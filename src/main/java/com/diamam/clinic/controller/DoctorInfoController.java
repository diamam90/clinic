package com.diamam.clinic.controller;

import com.diamam.clinic.model.DoctorInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для отображения информации по врачам
 */
@RestController
@RequestMapping("/api/v1/doctor-info")
public class DoctorInfoController {
    List<DoctorInfo> doctorInfoList = new ArrayList<>();

    {
        DoctorInfo doctor1 = new DoctorInfo(1L, "Ковырялко", "Красное-лечить", "хирург");
        DoctorInfo doctor2 = new DoctorInfo(2L, "Добрый", "Общеврач", "терапевт");
        DoctorInfo doctor3 = new DoctorInfo(3L, "Очкарёв", "Большие глаза", "окулист");
        doctorInfoList.addAll(List.of(doctor1, doctor2, doctor3));
    }

    @GetMapping
    public ResponseEntity<List<DoctorInfo>> doctors() {
        return ResponseEntity.ok(doctorInfoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> description(@PathVariable("id") Long id) {
        return ResponseEntity.of(doctorInfoList
                .stream()
                .filter(doctor -> doctor.id() == id)
                .findAny()
                .map(DoctorInfo::description));
    }
}
