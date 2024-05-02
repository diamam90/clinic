package com.diamam.clinic.service;

import com.diamam.clinic.model.appointment.AppointmentInfo;
import com.diamam.clinic.model.appointment.UpdateAppointmentDto;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    String make(UpdateAppointmentDto appointmentDto);

    String deleteById(String id);

    AppointmentInfo getById(String id);

    List<AppointmentInfo> getByClientId(String clientId);

    List<AppointmentInfo> getFreeSpotByDoctorIdAndDate(String doctorId, LocalDate date);

    List<AppointmentInfo> generateSchedule(String doctorId, LocalDate startDate, Integer days);
    List<AppointmentInfo> generateScheduleOneDay(String doctorId, LocalDate startDate);
}
