package com.diamam.doctorservice.controller;

import com.diamam.doctorservice.controller.swagger.DoctorControllerSwagger;
import com.diamam.doctorservice.entity.DoctorEntity;
import com.diamam.doctorservice.mapper.DoctorMapper;
import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.DoctorResponse;
import com.diamam.doctorservice.model.PatchDoctorRequest;
import com.diamam.doctorservice.model.UpdateDoctorRequest;
import com.diamam.doctorservice.service.DoctorService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController implements DoctorControllerSwagger {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping
    public DoctorResponse create(@RequestBody CreateDoctorRequest request) {
        return doctorMapper.toDto(doctorService.create(request));
    }

    @PostMapping("/{doctorId}")
    public DoctorResponse update(@PathVariable String doctorId, @RequestBody UpdateDoctorRequest request) {
        return doctorMapper.toDto(doctorService.update(doctorId, request));
    }

    @PatchMapping("/{doctorId}")
    public DoctorResponse patch(@PathVariable String doctorId, @RequestBody PatchDoctorRequest request) {
        return doctorMapper.toDto(doctorService.patch(doctorId, request));
    }

    @DeleteMapping("/{doctorId}")
    public void delete(@PathVariable String doctorId) {
        doctorService.deleteById(doctorId);
    }

    @GetMapping("/{doctorId}")
    public DoctorResponse getById(@PathVariable String doctorId) {
        return doctorMapper.toDto(doctorService.findById(doctorId));
    }

    @GetMapping("/find")
    public List<DoctorResponse> findBySpeciality(@RequestParam(value = "speciality") String speciality) {
        return doctorService.findBySpeciality(speciality).stream().map(doctorMapper::toDto).toList();
    }

    @GetMapping("/ids")
    public DoctorIdsResponse getDoctorIds(@RequestParam(required = false) LocalDateTime lastUpdated,

                                          @RequestParam
                                          @Positive(message = "Limit must be positive")
                                          @NotNull(message = "Limit must not be null")
                                          Integer limit) {
        var doctors = doctorService.findByLastUpdatedAndLimit(lastUpdated, limit);
        var hasNext = false;
        if (doctors.isEmpty()) {
            return new DoctorIdsResponse(Collections.emptyList(), lastUpdated, hasNext);
        }

        if (doctors.size() > limit) {
            doctors.removeLast();
            hasNext = true;
        }

        LocalDateTime last = doctors.getLast().getUpdatedAt();

        return new DoctorIdsResponse(
                doctors.stream().map(DoctorEntity::getId).toList(),
                last,
                hasNext
        );
    }

    public record DoctorIdsResponse(
            List<String> doctorIds,
            LocalDateTime lastUpdatedDateTime,
            boolean hasNext
    ) {
    }
}
