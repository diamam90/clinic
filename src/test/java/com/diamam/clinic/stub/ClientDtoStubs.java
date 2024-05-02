package com.diamam.clinic.stub;

import com.diamam.clinic.model.client.CreateClientDto;
import com.diamam.clinic.model.client.UpdateClientDto;

import java.time.LocalDate;

public class ClientDtoStubs {

    public static CreateClientDto createClient(){
        return new CreateClientDto(
                "Евгений",
                "Геннадьевич",
                "Зайцев",
                39,
                LocalDate.of(1980,12,1),
                "evgen@email.yandex",
                "12332122",
                "12345543212"
        );
    }

    public static UpdateClientDto updateClient(){
        return new UpdateClientDto(
                "123141231",
                "Александр",
                null,
                "Скворцов",
                27,
                LocalDate.of(2000,1,26),
                "alex@gmail.com",
                "88009000303",
                "1919111122"
        );
    }
}
