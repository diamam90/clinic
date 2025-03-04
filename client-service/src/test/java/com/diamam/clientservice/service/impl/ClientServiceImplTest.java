package com.diamam.clientservice.service.impl;

import com.diamam.clientservice.entity.ClientEntity;
import com.diamam.clientservice.mapper.ClientMapperImpl;
import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.repository.ClientRepository;
import com.diamam.clientservice.service.ClientService;
import com.diamam.clientservice.stub.ClientRequestStub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ClientServiceImpl.class, ClientMapperImpl.class})
class ClientServiceImplTest {
    @MockBean
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;

    @Test
    void create() {
        CreateClientRequest request = ClientRequestStub.createdIvan();
        ClientEntity savedClient = new ClientEntity();
        savedClient.setId("123s12");
        savedClient.setFirstName("Alex");
        savedClient.setLastName("Alexandrov");
        savedClient.setMiddleName("Alexandrovich");

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(savedClient);
        ClientEntity actual = clientService.create(request);
        assertThat(actual).hasFieldOrPropertyWithValue("firstName","Alex")
                .hasFieldOrPropertyWithValue("lastName","Alexandrov")
                .hasFieldOrPropertyWithValue("middleName","Alexandrovich")
                .hasFieldOrPropertyWithValue("id","123s12");

    }

    @Test
    void update() {
    }

    @Test
    void patch() {
    }

    @Test
    void delete() {
    }

    @Test
    void getById() {
    }
}