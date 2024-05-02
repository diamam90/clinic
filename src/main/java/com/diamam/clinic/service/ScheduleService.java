package com.diamam.clinic.service;

import java.time.LocalDate;

public interface ScheduleService {
    void generateScheduleNextWeek(LocalDate startDay, String doctorId);
    void generateScheduleCurrentDay(LocalDate currentDay, String doctorId);
}
