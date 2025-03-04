package com.diamam.clientservice.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String id) {
        super("Client with id {%s} was not found".formatted(id));
    }
}
