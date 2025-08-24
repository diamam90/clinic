package com.diamam.appointmentservice.service.impl;

import com.diamam.appointmentservice.entity.AppointmentEntity;
import com.diamam.appointmentservice.exception.BadRequestException;
import com.diamam.appointmentservice.dto.appointment.GenerateRequest;
import com.diamam.appointmentservice.dto.appointment.GenerateSingleDayRequest;
import com.diamam.appointmentservice.repository.AppointmentRepository;
import com.diamam.appointmentservice.service.GenerateAppointmentService;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class GenerateAppointmentServiceImpl implements GenerateAppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Value("${appointment-service.working-day-start:08:00:00}")
    private LocalTime workingDayStart;

    @Value("${appointment-service.working-day-end:20:00:00}")
    private LocalTime workingDayEnd;

    @Override
    public Map<LocalDate, List<AppointmentEntity>> generateByDoctorIdAndDaysCount(GenerateRequest request) {

        validateTimeShift(request.shiftStart(), request.shiftEnd(), request.duration());
        validateDate(request);

        var appointments = new ArrayList<AppointmentEntity>();

        for (int i = 0; i < request.days(); i++) {
            var currentDate = request.dateStart().plusDays(i);
            var currentDateAppointments = generateForDay(
                    request.doctorId(),
                    request.duration(),
                    currentDate,
                    request.shiftStart(),
                    request.shiftEnd());

            appointments.addAll(currentDateAppointments);
        }

        var savedAppointments = appointmentRepository.saveAll(appointments);
        return StreamEx.of(savedAppointments).sorted(appointmentComparator()).groupingBy(AppointmentEntity::getDate);
    }

    @Override
    public List<AppointmentEntity> generateByDoctorId(GenerateSingleDayRequest request) {

        validateTimeShift(request.shiftStart(), request.shiftEnd(), request.duration());
        validateDate(request);

        var generatedAppointments = generateForDay(
                request.doctorId(),
                request.duration(),
                request.date(),
                request.shiftStart(),
                request.shiftEnd()
        );

        return appointmentRepository.saveAll(generatedAppointments);
    }

    /**
     * Проверка что выбранная смена попадает в режим работы клиники
     *
     * @param shiftStart начало смены врача
     * @param shiftEnd   окончание смены врача
     * @param duration   длительность приема
     */
    private void validateTimeShift(LocalTime shiftStart, LocalTime shiftEnd, Duration duration) {
        if (!shiftTimeIsBetweenWorkingDay(shiftStart, shiftEnd) ||
                durationBetweenStartAndEndIsLessThanDuration(shiftStart, shiftEnd, duration)) {
            throw new BadRequestException("ShiftStart and shiftEnd must be between %s and %s"
                    .formatted(workingDayStart, workingDayEnd));
        }
    }


    private boolean shiftTimeIsBetweenWorkingDay(LocalTime shiftStart, LocalTime shiftEnd) {
        return shiftStart.isBefore(shiftEnd) &&
                !shiftStart.isBefore(workingDayStart) &&
                !shiftEnd.isAfter(workingDayEnd);
    }

    private boolean durationBetweenStartAndEndIsLessThanDuration(LocalTime shiftStart, LocalTime shiftEnd, Duration duration) {
        return Duration.between(shiftStart, shiftEnd).compareTo(duration) < 0;
    }

    /**
     * Проверка отсутствия приемов у врача в выбранный промежуток
     *
     * @param request запрос на создание расписания
     */
    private void validateDate(GenerateRequest request) {
        var endDate = request.dateStart().plusDays(request.days());

        if (appointmentRepository.existByDoctorAndDateBetween(
                request.doctorId(),
                request.dateStart(),
                endDate
        )) {
            throw new BadRequestException("Appointments for doctorId [%s] already exist for period [%s-%s]".
                    formatted(request.doctorId(), request.dateStart(), endDate));
        }
    }

    /**
     * Првоерка отсутствия приемов у врача по выбранной дате
     *
     * @param request запрос на создание расписания
     */
    private void validateDate(GenerateSingleDayRequest request) {
        if (appointmentRepository.existByDoctorAndDate(request.doctorId(), request.date())) {
            throw new BadRequestException("Appointments for doctorId [%s] already exist for date [%s]".
                    formatted(request.doctorId(), request.date()));
        }
    }


    /**
     * Генерация расписания для выбранного врача
     *
     * @param doctorId   идентификатор врача
     * @param duration   длительность приема
     * @param date       дата приема
     * @param shiftStart начало смены врача
     * @param shiftEnd   окончание смены врача
     * @return список приемов врача
     */
    private List<AppointmentEntity> generateForDay(String doctorId,
                                                   Duration duration,
                                                   LocalDate date,
                                                   LocalTime shiftStart,
                                                   LocalTime shiftEnd) {

        var appointmentsPerDay = (int) Duration.between(shiftStart, shiftEnd).dividedBy(duration);
        var appointments = new ArrayList<AppointmentEntity>(appointmentsPerDay);

        for (int j = 0; j < appointmentsPerDay; j++) {
            var appointment = new AppointmentEntity();
            var start = shiftStart.plus(duration.multipliedBy(j));
            var end = start.plus(duration);
            if (end.isAfter(shiftEnd)) {
                break;
            }

            appointment.setAvailable(true);
            appointment.setDoctorId(doctorId);
            appointment.setDate(date);
            appointment.setStart(start);
            appointment.setEnd(end);

            appointments.add(appointment);
        }

        return appointments;
    }

    private Comparator<AppointmentEntity> appointmentComparator() {
        return Comparator.comparing(AppointmentEntity::getDate)
                .thenComparing(AppointmentEntity::getStart);
    }
}
