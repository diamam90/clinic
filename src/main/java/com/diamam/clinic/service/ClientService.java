package com.diamam.clinic.service;

import com.diamam.clinic.entity.Client;
import com.diamam.clinic.model.client.CreateClientDto;
import com.diamam.clinic.model.client.UpdateClientDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client create(CreateClientDto clientDto);

    Client update(UpdateClientDto clientDto);

    void deleteById(String id);

    Client getById(String id);

    List<Client> findAll();
}
