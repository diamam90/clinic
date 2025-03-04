package com.diamam.clientservice.stub;

import com.diamam.clientservice.entity.ClientEntity;

public class ClientStub {

    public static ClientEntity ivan() {
        var client = new ClientEntity();
        client.setId("213dqsd1231");
        client.setFirstName("Ivan");
        client.setLastName("Ivanov");
        client.setMiddleName("Ivanovich");
        client.setAge(34);
        return client;
    }

    public static ClientEntity alex() {
        var client = new ClientEntity();
        client.setId("qwde123fsdr23");
        client.setFirstName("Alex");
        client.setLastName("Alexandrov");
        client.setMiddleName("Alexandrovich");
        return client;
    }
}
