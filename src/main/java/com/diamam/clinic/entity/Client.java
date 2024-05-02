package com.diamam.clinic.entity;

import com.diamam.clinic.model.client.UpdateClientDto;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "client")
public class Client {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String passport;

    public Client() {
    }


    public Client(String id, String firstName, String middleName, String lastName, Integer age, LocalDate birthDate,
                  String email, String phone, String passport) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.passport = passport;
    }

    public Client(String firstName, String middleName, String lastName, Integer age, LocalDate birthDate, String email,
                  String phone, String passport) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.passport = passport;
    }
}
