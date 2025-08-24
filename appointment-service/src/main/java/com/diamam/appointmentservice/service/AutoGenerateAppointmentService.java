package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.client.DoctorClient;
import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoGenerateAppointmentService {

    private final GenerateAppointmentService generateAppointmentService;
    private final DoctorClient doctorClient;
    private final Clock clock;

    @Value("${appointment-service.scheduled.batch-size:5}")
    private Integer batchSize;

    @Value("${appointment-service.scheduled.days-count:3}")
    private Integer daysCount;

    @Async("scheduledExecutor")
    @Scheduled(cron = "${appointment-service.scheduled.cron:-}")
    public void generateSchedule() {
        boolean hasNext = true;
        LocalDateTime lastProcessed = null;

        while (hasNext) {
            var doctorResponse = doctorClient.getDoctorIds(lastProcessed, batchSize);

            if (Objects.isNull(doctorResponse) || doctorResponse.doctorIds().isEmpty()) {
                break;
            }

            for (String id : doctorResponse.doctorIds()) {
                var request = new GenerateRequest(id, LocalDate.now(clock).plusDays(1), daysCount);
                try {
                    generateAppointmentService.generateByDoctorIdAndDaysCount(request);
                } catch (BadRequestException exception) {
                    log.warn(exception.getMessage());
                }
            }

            hasNext = doctorResponse.hasNext();
            lastProcessed = doctorResponse.lastUpdatedDateTime();
        }
    }
}
