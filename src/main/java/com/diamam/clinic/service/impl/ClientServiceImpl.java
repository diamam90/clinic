package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Client;
import com.diamam.clinic.model.client.CreateClientDto;
import com.diamam.clinic.model.client.UpdateClientDto;
import com.diamam.clinic.repository.ClientRepository;
import com.diamam.clinic.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client create(CreateClientDto clientDto) {
        Client client = new Client(
                clientDto.firstName(),
                clientDto.middleName(),
                clientDto.lastName(),
                clientDto.age(),
                clientDto.birthDate(),
                clientDto.email(),
                clientDto.phone(),
                clientDto.passport());
        return clientRepository.save(client);
    }

    @Override
    public Client update(UpdateClientDto clientDto) {
        clientRepository.findById(clientDto.id()).orElseThrow();
        Client client = new Client(
                clientDto.id(),
                clientDto.firstName(),
                clientDto.middleName(),
                clientDto.lastName(),
                clientDto.age(),
                clientDto.birthDate(),
                clientDto.email(),
                clientDto.phone(),
                clientDto.passport());
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(String id) {
        clientRepository.findById(id).orElseThrow();
        clientRepository.deleteById(id);
    }

    @Override
    public Client getById(String id) {
        return clientRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
