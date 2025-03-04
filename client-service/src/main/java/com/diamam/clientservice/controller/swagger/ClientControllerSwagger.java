package com.diamam.clientservice.controller.swagger;

import com.diamam.clientservice.model.ClientResponse;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

@Tag(name = "Операции над пользователями")
public interface ClientControllerSwagger {

    @Operation(summary = "Поиск клиента по идентификатору", method = "GET",
            parameters = {@Parameter(name = "Идентификатор клиента", in = ParameterIn.PATH, required = true,
                    schema = @Schema(implementation = String.class))},
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientResponse.class))),
                    @ApiResponse(responseCode = "400")})
    ClientResponse getById(String clientId);

    @Operation(summary = "Создание клиента", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    ClientResponse create(CreateClientRequest request);

    @Operation(summary = "Обновление клиента", method = "POST",
            parameters = @Parameter(name = "clientId", description = "Идентификатор клиента", in = ParameterIn.PATH,
                    required = true, schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "400")})
    ClientResponse update(String clientId, UpdateClientRequest request);

    @Operation(summary = "Частичное обновление клиента", method = "PATCH",
            parameters = @Parameter(name = "clientId", description = "Идентификатор клиента", in = ParameterIn.PATH,
                    required = true, schema = @Schema(implementation = String.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "400")})
    ClientResponse patch(String clientId, PatchClientRequest request);

    @Operation(summary = "Удаление клиента", method = "DELETE",
            parameters = {@Parameter(name = "clientId", description = "Идентификатор клиента", in = ParameterIn.PATH,
                    required = true, schema = @Schema(implementation = String.class))})
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400")})
    void delete(String clientId);
}
