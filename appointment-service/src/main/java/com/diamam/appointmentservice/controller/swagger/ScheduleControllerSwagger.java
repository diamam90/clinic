package com.diamam.appointmentservice.controller.swagger;

import com.diamam.appointmentservice.dto.appointment.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Операции над расписанием")
public interface ScheduleControllerSwagger {

    @Operation(summary = "Получение расписания врача на выбранную дату")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleResponse.class))))
    List<ScheduleResponse> findByDoctorIdAndDateBetween(String doctorId, LocalDate start, LocalDate end);
}
