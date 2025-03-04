package com.diamam.clientservice.stub;

import com.diamam.clientservice.model.CreateClientRequest;
import com.diamam.clientservice.model.PatchClientRequest;
import com.diamam.clientservice.model.UpdateClientRequest;

public class ClientRequestStub {

    public static CreateClientRequest createdIvan() {
        return new CreateClientRequest(
                "Alex",
                "Alexandrov",
                "Alexandrovich");
    }

    public static UpdateClientRequest updatedIvan() {
        return new UpdateClientRequest(
                "Ivan",
                "Ivanov",
                "Ivanovich",
                34,
                "1000 1231231",
                "88002000600",
                "ivan@ivanov@yandex.ru");
    }

    public static PatchClientRequest patchedIvan() {
        return new PatchClientRequest(
                "Ivan",
                "Ivanov",
                "Ivanovich",
                34,
                "1000 1231231",
                "88002000600",
                "ivan@ivanov@yandex.ru");
    }
}
