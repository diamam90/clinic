package com.diamam.clientservice.controller;

import com.diamam.clientservice.controller.swagger.ClientControllerSwagger;
import com.diamam.clientservice.mapper.ClientMapper;
import com.diamam.clientservice.model.ClientResponse;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;
import com.diamam.clientservice.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController implements ClientControllerSwagger {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@RequestBody @Valid CreateClientRequest request) {
        return clientMapper.toDto(clientService.create(request));
    }

    @PostMapping("/{clientId}")
    public ClientResponse update(@PathVariable String clientId, @RequestBody UpdateClientRequest request) {
        return clientMapper.toDto(clientService.update(clientId, request));
    }

    @PatchMapping("/{clientId}")
    public ClientResponse patch(@PathVariable String clientId, @RequestBody PatchClientRequest request) {
        return clientMapper.toDto(clientService.patch(clientId, request));
    }

    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String clientId) {
        clientService.delete(clientId);
    }

    @GetMapping("/{clientId}")
    public ClientResponse getById(@PathVariable String clientId) {
        return clientMapper.toDto(clientService.getById(clientId));
    }
}
