package com.diamam.clientservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("clients")
public class ClientEntity {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer age;
    private String passport;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
