package com.diamam.clientservice.repository;

import com.diamam.clientservice.entity.ClientEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface ClientRepository extends ListCrudRepository<ClientEntity, String> {
}
