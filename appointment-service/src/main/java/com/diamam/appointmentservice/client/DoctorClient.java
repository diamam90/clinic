package com.diamam.appointmentservice.client;

import com.diamam.appointmentservice.model.doctorservice.DoctorIdsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DoctorClient {

    private final RestClient restClient;

    public DoctorIdsResponse getDoctorIds(LocalDateTime lastProcessedUpdateDateTime, Integer batchSize) {
        return restClient.get()
                .uri("/api/v1/doctors/ids?lastProcessed={lastProcessedUpdateTime}&limit={batchSize}", lastProcessedUpdateDateTime, batchSize)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(DoctorIdsResponse.class)
                .getBody();
    }

}
