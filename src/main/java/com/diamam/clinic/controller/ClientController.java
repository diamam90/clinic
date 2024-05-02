package com.diamam.clinic.controller;

import com.diamam.clinic.model.client.ClientInfo;
import com.diamam.clinic.model.client.CreateClientDto;
import com.diamam.clinic.model.client.UpdateClientDto;
import com.diamam.clinic.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ClientInfo createClient(@Valid @RequestBody CreateClientDto clientDto){
        return ClientInfo.toDto(clientService.create(clientDto));
    }

    @GetMapping
    public List<ClientInfo> findAllClients(){
        return clientService.findAll().stream().map(ClientInfo::toDto).toList();
    }

    @GetMapping("/{clientId}")
    public ClientInfo getClientById(@PathVariable("clientId") String clientId){
        return ClientInfo.toDto(clientService.getById(clientId));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteById(@PathVariable("clientId") String clientId){
        clientService.deleteById(clientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ClientInfo updateClient(@Valid @RequestBody UpdateClientDto clientDto){
        return ClientInfo.toDto(clientService.update(clientDto));
    }

}
