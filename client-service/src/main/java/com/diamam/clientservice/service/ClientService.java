package com.diamam.clientservice.service;

import com.diamam.clientservice.entity.ClientEntity;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;

public interface ClientService {
    ClientEntity create(CreateClientRequest createRequest);

    ClientEntity update(String clientId, UpdateClientRequest updateRequest);

    ClientEntity patch(String clientId, PatchClientRequest updateRequest);

    void delete(String id);

    ClientEntity getById(String clientId);
}
