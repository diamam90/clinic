package com.diamam.clinic.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static class DoctorNotFoundException extends NotFoundException {
        public DoctorNotFoundException(String message) {
            super(message);
        }

        public static DoctorNotFoundException byId(String id) {
            return new DoctorNotFoundException("Doctor with id %s is not found".formatted(id));
        }
    }

    public static class ClientNotFoundException extends NotFoundException {
        public ClientNotFoundException(String message) {
            super(message);
        }

        public static ClientNotFoundException byId(String id) {
            return new ClientNotFoundException("Client with id %s is not found".formatted(id));
        }
    }

    public static class AppointmentNotFoundException extends NotFoundException {
        public AppointmentNotFoundException(String message) {
            super(message);
        }

        public static AppointmentNotFoundException byId(String id) {
            return new AppointmentNotFoundException("Appointment with id %s is not found".formatted(id));
        }
    }



}
