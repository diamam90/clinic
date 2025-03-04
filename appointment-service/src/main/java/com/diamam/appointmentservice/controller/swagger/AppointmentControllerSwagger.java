package com.diamam.appointmentservice.controller.swagger;

import com.diamam.appointmentservice.model.AppointmentResponse;
import com.diamam.appointmentservice.model.CreateAppointmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Операции над приемами")
public interface AppointmentControllerSwagger {

    @Operation(description = "Создание приема", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400")
    })
    AppointmentResponse create(CreateAppointmentRequest request);

    @Operation(description = "Поиск приема по идентификатору", method = "GET", parameters = @Parameter(in = ParameterIn.PATH))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400")
    })
    AppointmentResponse findById(Long appointmentId);

    @Operation(description = "Поиск приемов по идентификатору доктора и дате", method = "GET",
            parameters = {@Parameter(name = "doctorId", in = ParameterIn.PATH),
                    @Parameter(name = "date", in = ParameterIn.PATH)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentResponse.class)))),
            @ApiResponse(responseCode = "400")
    })
    List<AppointmentResponse> findByDoctorIdAndDate(String doctorId, LocalDate date);

    @Operation(description = "Поиск приемов по идентификатору клиента", method = "GET",
            parameters = @Parameter(name = "clientId", in = ParameterIn.PATH))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentResponse.class)))),
            @ApiResponse(responseCode = "400")
    })
    List<AppointmentResponse> findByClientId(String clientId);

    @Operation(description = "Отмена приема по идентификатору", method = "DELETE",
            parameters = @Parameter(name = "appointmentId", in = ParameterIn.PATH))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400")
    })
    Long cancel(Long appointmentId);
}
