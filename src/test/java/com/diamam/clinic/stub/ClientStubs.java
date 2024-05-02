package com.diamam.clinic.stub;

import com.diamam.clinic.entity.Client;

import java.time.LocalDate;

public class ClientStubs {

    public static Client alex() {
        Client alex = new Client();
        alex.setFirstName("Alex");
        alex.setMiddleName("Alexandrovich");
        alex.setLastName("Alexandrov");
        alex.setAge(22);
        alex.setBirthDate(LocalDate.of(2000, 1, 1));
        alex.setEmail("alex@alex.al");
        alex.setPhone("8-800-2000-600");
        alex.setPassport("1818290912");
        return alex;
    }

    public static Client boris() {
        Client boris = new Client();
        boris.setFirstName("Borya");
        boris.setMiddleName("Ivanovich");
        boris.setLastName("Borisov");
        boris.setAge(19);
        boris.setBirthDate(LocalDate.of(2005, 6, 5));
        boris.setEmail("boris228@yandex.ru");
        boris.setPhone("8-123-456-7890");
        boris.setPassport("2012290911");
        return boris;
    }

    public static Client savedAlex() {
        Client alex = new Client();
        alex.setFirstName("Alex");
        alex.setMiddleName("Alexandrovich");
        alex.setLastName("Alexandrov");
        alex.setAge(22);
        alex.setBirthDate(LocalDate.of(2000, 1, 1));
        alex.setEmail("alex@alex.al");
        alex.setPhone("8-800-2000-600");
        alex.setPassport("1818290912");
        alex.setId("16");
        return alex;
    }

    public static Client savedBoris() {
        Client boris = new Client();
        boris.setFirstName("Borya");
        boris.setMiddleName("Ivanovich");
        boris.setLastName("Borisov");
        boris.setAge(19);
        boris.setBirthDate(LocalDate.of(2005, 6, 5));
        boris.setEmail("boris228@yandex.ru");
        boris.setPhone("8-123-456-7890");
        boris.setPassport("2012290911");
        boris.setId("12");
        return boris;
    }
}
