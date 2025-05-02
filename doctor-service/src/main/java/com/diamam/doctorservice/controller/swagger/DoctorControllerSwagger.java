package com.diamam.doctorservice.controller.swagger;

import com.diamam.doctorservice.model.CreateDoctorRequest;
import com.diamam.doctorservice.model.DoctorResponse;
import com.diamam.doctorservice.model.PatchDoctorRequest;
import com.diamam.doctorservice.model.UpdateDoctorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.util.List;

@Tag(name = "Операции над докторами")
public interface DoctorControllerSwagger {

    @Operation(summary = "Создание доктора", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorResponse.class))),
            @ApiResponse(responseCode = "400")})
    DoctorResponse create(CreateDoctorRequest request);

    @Operation(summary = "Обновление доктора", method = "POST",
            parameters = @Parameter(name = "doctorId", in = ParameterIn.PATH, required = true))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorResponse.class))),
            @ApiResponse(responseCode = "400")})
    DoctorResponse update(String doctorId, UpdateDoctorRequest request);

    @Operation(summary = "Частичное обновление доктора", method = "PATCH",
            parameters = @Parameter(name = "doctorId", in = ParameterIn.PATH, required = true))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorResponse.class))),
            @ApiResponse(responseCode = "400")})
    DoctorResponse patch(String doctorId, PatchDoctorRequest request);

    @Operation(summary = "Удаление доктора", method = "DELETE",
            parameters = @Parameter(name = "doctorId", in = ParameterIn.PATH, required = true))
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400")})
    void delete(String id);

    @Operation(summary = "Поиск доктора по идентификатору", method = "GET",
            parameters = @Parameter(name = "doctorId", in = ParameterIn.PATH, required = true))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorResponse.class))),
            @ApiResponse(responseCode = "400")})
    DoctorResponse getById(String doctorId);

    @Operation(summary = "Поиск докторов по специальности", method = "GET", parameters = @Parameter(in = ParameterIn.QUERY, name = "speciality"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,array = @ArraySchema(schema = @Schema(implementation = DoctorResponse.class))))
    })
    List<DoctorResponse> findBySpeciality(String speciality);
}
