package com.diamam.clinic.service.impl;

import com.diamam.clinic.entity.Client;
import com.diamam.clinic.model.client.CreateClientDto;
import com.diamam.clinic.model.client.UpdateClientDto;
import com.diamam.clinic.repository.ClientRepository;
import com.diamam.clinic.stub.ClientDtoStubs;
import com.diamam.clinic.stub.ClientStubs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    ClientServiceImpl clientService;
    @Captor
    ArgumentCaptor<Client> clientArgumentCaptor;


    @Test
    void create() {
        CreateClientDto dto = ClientDtoStubs.createClient();
        clientService.create(dto);
        verify(clientRepository).save(clientArgumentCaptor.capture());

        assertThat(clientArgumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("firstName", dto.firstName())
                .hasFieldOrPropertyWithValue("middleName", dto.middleName())
                .hasFieldOrPropertyWithValue("lastName", dto.lastName())
                .hasFieldOrPropertyWithValue("email", dto.email())
                .hasFieldOrPropertyWithValue("age", dto.age())
                .hasFieldOrPropertyWithValue("phone", dto.phone())
                .hasFieldOrPropertyWithValue("birthDate", dto.birthDate())
                .hasFieldOrPropertyWithValue("passport", dto.passport());
    }

    @Test
    void update() {
        UpdateClientDto forUpdate = ClientDtoStubs.updateClient();

        Client client = ClientStubs.savedAlex();
        client.setId(forUpdate.id());

        when(clientRepository.findById(forUpdate.id())).thenReturn(Optional.of(client));

        clientService.update(forUpdate);
        verify(clientRepository).save(clientArgumentCaptor.capture());

        assertThat(clientArgumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id", forUpdate.id())
                .hasFieldOrPropertyWithValue("firstName", forUpdate.firstName())
                .hasFieldOrPropertyWithValue("middleName", forUpdate.middleName())
                .hasFieldOrPropertyWithValue("lastName", forUpdate.lastName())
                .hasFieldOrPropertyWithValue("age", forUpdate.age())
                .hasFieldOrPropertyWithValue("birthDate", forUpdate.birthDate())
                .hasFieldOrPropertyWithValue("email", forUpdate.email())
                .hasFieldOrPropertyWithValue("phone", forUpdate.phone())
                .hasFieldOrPropertyWithValue("passport", forUpdate.passport());

    }

    @Test
    void deleteById() {
        Client alex = ClientStubs.savedAlex();
        when(clientRepository.findById(alex.getId())).thenReturn(Optional.of(alex));
        clientService.deleteById(alex.getId());
        verify(clientRepository).deleteById(alex.getId());
    }

    @Test
    void getById() {
        Client client = ClientStubs.savedAlex();
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        clientService.getById(client.getId());
    }

    @Test
    void findAll() {
        List<Client> clients = List.of(ClientStubs.savedAlex(), ClientStubs.savedBoris());
        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> actual = clientService.findAll();
        assertThat(actual).isEqualTo(clients);
    }
}