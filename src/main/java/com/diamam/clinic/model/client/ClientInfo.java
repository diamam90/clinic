package com.diamam.clinic.model.client;

import com.diamam.clinic.entity.Client;

public record ClientInfo(
        String id,
        String firstName,
        String middleName,
        String lastName,
        Integer age
) {

    public static ClientInfo toDto(Client client){
        return new ClientInfo(
                client.getId(),
                client.getFirstName(),
                client.getMiddleName(),
                client.getLastName(),
                client.getAge()
        );
    }
}
