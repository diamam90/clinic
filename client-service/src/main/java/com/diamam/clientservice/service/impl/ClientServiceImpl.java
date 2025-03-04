package com.diamam.clientservice.service.impl;

import com.diamam.clientservice.entity.ClientEntity;
import com.diamam.clientservice.exception.ObjectNotFoundException;
import com.diamam.clientservice.mapper.ClientMapper;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;
import com.diamam.clientservice.repository.ClientRepository;
import com.diamam.clientservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientEntity create(CreateClientRequest clientRequest) {
        return clientRepository.save(clientMapper.fromCreateRequest(clientRequest));
    }

    @Override
    public ClientEntity update(String clientId, UpdateClientRequest updateRequest) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException(clientId));
        client.setFirstName(updateRequest.firstName());
        client.setLastName(updateRequest.lastName());
        client.setMiddleName(updateRequest.middleName());
        client.setAge(updateRequest.age());
        client.setPassport(updateRequest.passport());
        client.setPhone(updateRequest.phone());
        client.setEmail(updateRequest.email());
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    @Override
    public ClientEntity patch(String clientId, PatchClientRequest updateRequest) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException(clientId));
        Optional.ofNullable(updateRequest.firstName()).ifPresent(client::setFirstName);
        Optional.ofNullable(updateRequest.lastName()).ifPresent(client::setLastName);
        Optional.ofNullable(updateRequest.middleName()).ifPresent(client::setMiddleName);
        Optional.ofNullable(updateRequest.age()).ifPresent(client::setAge);
        Optional.ofNullable(updateRequest.passport()).ifPresent(client::setPassport);
        Optional.ofNullable(updateRequest.phone()).ifPresent(client::setPhone);
        Optional.ofNullable(updateRequest.email()).ifPresent(client::setEmail);
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    @Override
    public void delete(String id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientEntity getById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException(clientId));
    }
}
