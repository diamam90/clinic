package com.diamam.clinic.repository;

import com.diamam.clinic.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client,String> {
}
