package com.diamam.appointmentservice.service;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.dto.appointment.GenerateSingleDayRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Сервис для создания расписания врача
 */
public interface GenerateAppointmentService {

    /**
     * Создание расписания для врача на выбранное количество дней
     * @param request запрос на создание расписания
     * @return Расписание, группированное по дням
     */
    Map<LocalDate, List<AppointmentEntity>> generateByDoctorIdAndDaysCount(GenerateRequest request);

    /**
     * Создание расписания для врача на выбранную дату
     * @param request запрос на создания расписания
     * @return Расписание врача
     */
    List<AppointmentEntity> generateByDoctorId(GenerateSingleDayRequest request);

}
