package com.diamam.appointmentservice.mapper;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.dto.appointment.AppointmentResponse;
import com.diamam.appointmentservice.dto.appointment.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppointmentScheduleMapper {

    private final AppointmentMapper appointmentMapper;

    public List<ScheduleResponse> toDto(List<AppointmentEntity> appointments) {

        var sortedByDate = appointments.stream()
                .collect(Collectors.groupingBy(AppointmentEntity::getDate));

        var result = new ArrayList<ScheduleResponse>(sortedByDate.size());

        for (Map.Entry<LocalDate, List<AppointmentEntity>> entry : sortedByDate.entrySet()) {
            boolean isAvailable = false;

            var appointmentResponseList = new ArrayList<AppointmentResponse>(entry.getValue().size());

            for (int i = 0; i < entry.getValue().size(); i++) {
                var entity = entry.getValue().get(i);
                if (entity.isAvailable()) {
                    isAvailable = true;
                }

                var appointmentResponse = appointmentMapper.toDto(entity);
                appointmentResponseList.add(appointmentResponse);
            }

            var scheduleResponse = new ScheduleResponse(
                    entry.getKey(),
                    appointmentResponseList,
                    isAvailable
            );

            result.add(scheduleResponse);
        }

        return result;
    }
}
