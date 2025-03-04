package com.diamam.clientservice.mapper;

import com.diamam.clientservice.entity.ClientEntity;
import com.diamam.clientservice.model.ClientResponse;
import com.diamam.clientservice.model.CreateClientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface ClientMapper {

    ClientResponse toDto(ClientEntity client);

    @Mapping(target = "createdAt", expression ="java(LocalDateTime.now())" )
    ClientEntity fromCreateRequest(CreateClientRequest request);
}


