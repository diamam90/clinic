package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Client;
import com.diamam.clinic.stub.ClientStubs;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ClientRepositoryTest extends AbstractRepositoryTest {


    @BeforeEach
    void clear() {
        mongoOperations.dropCollection("client");
    }

    @Test
    void shouldCreateClient() {
        Client client = ClientStubs.alex();
        clientRepository.save(client);

        List<Client> clients = mongoOperations.find(Query.query(new Criteria()), Client.class);
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0))
                .usingRecursiveComparison(
                        RecursiveComparisonConfiguration.
                                builder()
                                .withIgnoredFields("id")
                                .build())
                .isEqualTo(client);
    }

    @DisplayName("Test update client functionality")
    @Test
    void shouldUpdateClient() {
        Client alex = ClientStubs.alex();
        Client client = mongoOperations.save(alex, "client");
        List<Client> beforeUpdate = mongoOperations.find(Query.query(new Criteria()), Client.class);

        Client boris = ClientStubs.boris();
        boris.setId(client.getId());

        clientRepository.save(boris);
        List<Client> afterUpdate = mongoOperations.find(Query.query(new Criteria()), Client.class);

        assertThat(beforeUpdate).hasSize(1);
        assertThat(beforeUpdate.get(0)).usingRecursiveComparison(
                RecursiveComparisonConfiguration
                        .builder()
                        .withIgnoredFields("id")
                        .build()).isEqualTo(alex);

        assertThat(afterUpdate).hasSize(1);
        assertThat(afterUpdate.get(0)).isEqualTo(boris);
    }


    @Test
    void shouldDeleteClient() {
        Client alex = ClientStubs.alex();
        Client saved = mongoOperations.save(alex, "client");

        List<Client> beforeDelete = mongoOperations.find(Query.query(new Criteria()), Client.class);
        clientRepository.deleteById(saved.getId());
        List<Client> afterDelete = mongoOperations.find(Query.query(new Criteria()), Client.class);

        assertThat(beforeDelete).hasSize(1);
        assertThat(afterDelete).isEmpty();
    }

    @Test
    void shouldFindClientById() {
        Client alex = ClientStubs.alex();
        Client saved = mongoOperations.save(alex, "client");

        Optional<Client> optional = clientRepository.findById(saved.getId());

        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(saved);
    }

    @Test
    void shouldFindTwoClients() {
        Client alex = ClientStubs.alex();
        Client boris = ClientStubs.boris();

        mongoOperations.save(alex, "client");
        mongoOperations.save(boris, "client");

        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(2);
    }
}