package com.diamam.doctorservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("doctors")
public class DoctorEntity {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String speciality;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
