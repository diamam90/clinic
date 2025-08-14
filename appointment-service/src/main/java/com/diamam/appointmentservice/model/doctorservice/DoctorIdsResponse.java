package com.diamam.appointmentservice.model.doctorservice;


import java.time.LocalDateTime;
import java.util.List;

public record DoctorIdsResponse(
        List<String> doctorIds,
        LocalDateTime lastUpdatedDateTime,
        boolean hasNext
) {

}
