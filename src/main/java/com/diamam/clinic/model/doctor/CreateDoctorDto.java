package com.diamam.clinic.model.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDoctorDto(
        @NotNull(message = "Имя не указано")
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 20, message = "Имя должно быть длиной от {min} до {max} символов")
        String firstName,
        @Size(min = 2, max = 20, message = "Отчество должно быть длиной от {min} до {max} символов")
        String middleName,
        @NotNull(message = "Фамилия не указана")
        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(min = 2, max = 20, message = "Фамилия должна быть длиной от {min} до {max} символов")
        String lastName,
        @NotNull(message = "Специальность не указана")
        @NotBlank(message = "Специальность не должна быть пустой")
        @Size(min = 2, max = 20, message = "Специальность должна быть длиной от {min} до {max} символов")
        String speciality,
        @NotBlank(message ="Описание не должно быть пустым")
        String description,
        @NotNull(message = "Статус не указан")
        @NotBlank(message = "Статус не должен быть пустым")
        String status,
        Double rating) {

    public CreateDoctorDto(String firstName, String middleName, String lastName, String speciality, String description, String status) {
        this(firstName, middleName, lastName, speciality, description, status, 0.0);
    }

        public CreateDoctorDto(String firstName,  String lastName, String speciality, String description, String status) {
                this(firstName, null, lastName, speciality, description, status, 0.0);
        }
}
