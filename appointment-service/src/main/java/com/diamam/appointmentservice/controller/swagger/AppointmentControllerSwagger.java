package com.diamam.appointmentservice.controller.swagger;

import com.diamam.appointmentservice.dto.appointment.AppointmentFilter;
import com.diamam.appointmentservice.dto.appointment.AppointmentResponse;
import com.diamam.appointmentservice.dto.appointment.ReserveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Операции над приемами")
public interface AppointmentControllerSwagger {

    @Operation(description = "Создание приема", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400")
    })
    AppointmentResponse reserve(@Positive Long appointmentId, @RequestBody @Valid ReserveRequest request);

    @Operation(description = "Отмена приема по идентификатору", method = "DELETE",
            parameters = @Parameter(name = "appointmentId", in = ParameterIn.PATH))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400")
    })
    AppointmentResponse cancel(@Positive Long appointmentId);

    @Operation(description = "Поиск приема по идентификатору", method = "GET", parameters = @Parameter(in = ParameterIn.PATH))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.class))),
            @ApiResponse(responseCode = "400")
    })
    AppointmentResponse findById(@Positive Long appointmentId);

    @Operation(
            description = "Поиск приемов по фильтру",
            method = "GET",
            parameters = @Parameter(
                    in = ParameterIn.QUERY,
                    content = @Content(schema = @Schema(implementation = AppointmentFilter.class))
            )
    )
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppointmentResponse.class)))
    List<AppointmentResponse> filter(@Valid AppointmentFilter filter);
}
