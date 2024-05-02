package com.diamam.clinic.model.appointment;

import com.diamam.clinic.entity.Client;

public record AppointmentClient (String id, String fullName){
    public static AppointmentClient forAppointment(Client client){
        return new AppointmentClient(
                client.getId(),
                String.join(" ", client.getLastName(),client.getFirstName(),client.getMiddleName()));
    }
}
